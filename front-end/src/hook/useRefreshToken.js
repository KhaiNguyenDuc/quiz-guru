import AuthService from "../services/AuthService";

const useRefreshToken = () => {
    const refreshToken = localStorage.getItem("refreshToken");
    return AuthService.refreshToken(refreshToken);
};

export default useRefreshToken;
