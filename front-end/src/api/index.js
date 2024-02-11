import axios from "axios";
import useRefreshToken from "../hook/useRefreshToken";
import { validateJwtToken } from "../utils/Utils";

const axiosPrivate = axios.create({
    baseURL: "http://localhost:8080",
  });
  


axiosPrivate.interceptors.request.use(
   (request) => {
    const accessToken = localStorage.getItem('accessToken');
    // const refreshToken = localStorage.getItem('refreshToken');
    // if(!validateJwtToken(accessToken) || !validateJwtToken(refreshToken)){
    //   window.location.href = "/auth/login"
    //   return;
    // }

    request.headers["Authorization"] = `Bearer ${accessToken}`;
    return request;
  },
  (error) => Promise.reject(error)
);

axiosPrivate.interceptors.response.use(
  (response) => {

    return response;
  },
  async (error) => {
    const prevRequest = error?.config;
    if (
      (error.response.status === 401 && localStorage.getItem('refreshToken')) &&
      !prevRequest?.sent
    ) {
      prevRequest.sent = true;

      const refresh = await useRefreshToken();

      if (refresh?.refreshToken && refresh?.accessToken) {
        
        localStorage.setItem("accessToken", refresh?.accessToken);
        localStorage.setItem("refreshToken", refresh?.refreshToken);

        return axiosPrivate(prevRequest);
      } else {
 
        localStorage.clear()
        window.location.href = "/auth/login"
      }
    } else if (error.response.status === 500){

      window.location.href = "/internal-error" 
    } else{
      return Promise.reject(error)
    }
  }
);

export { axiosPrivate };
