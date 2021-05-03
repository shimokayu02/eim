import axios from "axios";
axios.defaults.withCredentials = true; // see https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS#Access-Control-Allow-Credentials
export default {
    logined(v) {
        axios
            .get("/authentication/loginStatus")
            .then(v, console.log("同期中"))
            .catch(error => {
                console.log(error);
            });
    },
    getOperatorInfo(v) {
        axios
            .get("/authentication")
            .then(v, console.log("同期中"))
            .catch(error => {
                console.log(error);
            });
    },
};