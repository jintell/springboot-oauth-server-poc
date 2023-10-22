import {useNavigate, useSearchParams} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from 'axios';

const baseUrl = "http://127.0.0.1:8080/auth/code/endpoint";
export const ProcessLogin = () => {
    const [searchParams] = useSearchParams();
    const code = searchParams.get('code');
    const deviceId = searchParams.get('state');
    const[accessToken, setAccessToken] = useState({});
    const[status, setStatus] = useState(200);
    const navigate = useNavigate();


    // The function will invoke when the user changes the tab

    useEffect(() => {
        const authenticate = async () => {
            const url = `${baseUrl}?code=${code}&deviceId=${deviceId}`;
            await axios
                .post(url, null, {})
                .then((resp) => (setAccessToken(resp.data)))
                .catch((e) => {
                    setStatus(400);
                    console.log(e)
                });
        }
        authenticate()
            .finally(() => console.log('Done getting authentication!'));
    }, [code, deviceId]);

    useEffect(() => {
        if(Object.keys(accessToken).length === 0) console.log("Could not get access token!");
        else {
            localStorage.setItem("token", JSON.stringify(accessToken)); // Should encrypt before storing in production
            navigate('/dashboard');
        }

    }, [accessToken, navigate]);

    return (
        <div className="container">
            <div className="alert-danger" style={{ display: status === 400 ? 'block' : 'none', padding: '1%' }}>
                <h4 style={{ textAlign: 'center' }}>Login Processing Error</h4>
                <p style={{ textAlign: 'center' }}>
                    Sorry we could not verify your request currently. Please try later<br /><br />
                    <a href="/" style={{ padding: '1%', backgroundColor: '#1f66f5', color: 'white', textDecoration: 'none' }}>
                        Go to Home
                    </a>
                    <br />
                </p>
            </div>
            <h1 style={{ display: status === 400 ? 'none' : 'block' }}>Processing....</h1>
        </div>
    );
}