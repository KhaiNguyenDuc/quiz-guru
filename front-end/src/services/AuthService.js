import axios from "axios";
import { AUTH_URL } from "../utils/Constant";
import { isValidEmail } from "../utils/Utils";


class AuthService{
    login(username, password){
        let credentials = {} 
        if(isValidEmail(username)){
            credentials = {
                email: username,
                password: password
            } 
        }else{
            credentials = {
                username: username,
                password: password
            }
        }
        return axios
        .post(AUTH_URL + "/login", credentials)
        .then((response) => response?.data?.data)
        .catch((error) => error?.response);
    }

    register(registerInfo){
        return axios
        .post(AUTH_URL + "/register", registerInfo)
        .then((response) => response)
        .catch((error) => error?.response);
    }

    refreshToken(refreshToken){
        return axios.post(AUTH_URL + "/refresh-token", {
            refreshToken: refreshToken
        })
        .then((response) => response?.data)
        .catch((error) => error?.response);
    }

    sendResetPassword(email){
        return axios
        .post(AUTH_URL + "/send-reset-password", {
            email: email
        })
        .then((response) => response?.data?.data)
        .catch((error) => error?.response);
    }

    resetPassword(request){
        return axios
        .post(AUTH_URL + "/reset-password", {
            email: request?.email,
            token: request?.token,
            password: request?.password
        })
        .then((response) => response)
        .catch((error) => error?.response);
    }

    sendVerify(username){
        return axios
        .get(AUTH_URL + "/send-verify?username=" + username)
        .then((response) => response)
        .catch((error) => error?.response);
    }

    verify(username, token){
        return axios
        .post(AUTH_URL + "/verify", {
            username: username,
            token: token
        })
        .then((response) => response)
        .catch((error) => error?.response);
    }

}

export default new AuthService()