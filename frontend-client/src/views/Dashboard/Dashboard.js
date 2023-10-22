import {useEffect, useState} from "react";
import axios from "axios";
import './style.css';

const logoutEndpoint = "http://127.0.0.1:8080/auth/code/logout";
export const Dashboard = () => {
    const [token, setToken] = useState({access_token: ''});
    const [signOutUrl, setSignOutUrl] = useState('/');

    const signOut = () => {
        window.location.replace(signOutUrl);
    }

    const greetings = async (priv) => {
        let url = (priv === 1)? "http://127.0.0.1:8080/hello" : "http://127.0.0.1:8080/hello-admin";
        await axios
            .get(url, {
                headers: {
                    Authorization: `Bearer ${token.access_token}`,
                    Accept: '*/*'
                }
            })
            .then((resp) => ( console.log(resp.data) ))
            .catch((e) => {
                console.log(e)
            });
    }

    useEffect(() => {
        const access = localStorage.getItem("token");
        try {
            setToken(JSON.parse(access));
        }catch (e) {
            console.log(e);
        }
    }, []);

    useEffect(() => {
        const getSignOutUrl = async () => {
            await axios.get(logoutEndpoint, {})
                .then(resp => ( setSignOutUrl(resp.data) ))
                .catch(e => console.log(e));
        }
        getSignOutUrl();
    }, []);

    return (
        <div className="container">
            <div style={{ overflow: 'hidden' }}>
                <span style={{ float: "left", }}>
                    <h1>POC Dash Board</h1>
                </span>
                <span style={{ float: "right", }}>
                    <button className="button-logout" onClick={signOut}>
                        Sign Out
                    </button>
                </span>
            </div>
            <hr />
            <div>
                <button className="button-activity" onClick={() => greetings(1)}>
                    Hello
                </button>
                <button className="button-activity" onClick={()=>greetings(2)}>
                    Hello-Admin
                </button>
            </div>
        </div>
    )
}