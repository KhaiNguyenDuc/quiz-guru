import axios from "axios";
import { AUTH_URL } from "../utils/Constant";


class AuthService{
    login(username, password){
        return axios
        .post(AUTH_URL + "/login", {
            username: username,
            password: password
        })
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
}

export default new AuthService()