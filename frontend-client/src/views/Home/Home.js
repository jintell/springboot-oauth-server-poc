import {useEffect, useState} from "react";
import axios from "axios";
import {v4 as uuid} from 'uuid';

export const TAB_ID_KEY = 'tabId'
export const Home = () => {
    const[authUrl,setAuthUrl] = useState('');


        // The function will invoke when the user changes the tab
        console.log(sessionStorage.getItem(TAB_ID_KEY));
        useEffect(() => {
            if (!sessionStorage.getItem(TAB_ID_KEY)) {
                sessionStorage.setItem(TAB_ID_KEY, uuid())
            }
        }, [])

        useEffect(() => {
            const getAuthorizedUrl = async () => {
                const url = `http://127.0.0.1:8080/request/authorize/url/${sessionStorage.getItem(TAB_ID_KEY)}`;
                await axios
                    .get(url, {})
                    .then((resp) => ( setAuthUrl(resp.data) ))
                    .catch((e) => {
                        console.log(e)
                    });
            }
            getAuthorizedUrl()
                .finally(() => console.log('Done authorizing!'));

        }, []);

        useEffect(() => {
            window.location.replace(authUrl);
        }, [authUrl]);

        return (
            <div>
                <h3>Hold while we get you started...</h3>
            </div>
        );
}