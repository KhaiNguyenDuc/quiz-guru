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
    console.log("innnnnn")
    const prevRequest = error?.config;
    if (
      (error.response.status === 401 && localStorage.getItem('refreshToken')) &&
      !prevRequest?.sent
    ) {
      prevRequest.sent = true;
      
      console.log("Refresh access Token")
      const refresh = await useRefreshToken();
      console.log(refresh)  
      if (refresh?.refreshToken && refresh?.accessToken) {
        
        localStorage.setItem("accessToken", refresh?.accessToken);
        localStorage.setItem("refreshToken", refresh?.refreshToken);

        return axiosPrivate(prevRequest);
      } else {
        console.log("Hết cứu")
        localStorage.clear()
        window.location.href = "/auth/login"
      }
    } else if (error.response.status === 500){
      console.log("500 rui")
      console.log(error)
      window.location.href = "/internal-error" 
    } else{
      return Promise.reject(error)
    }
  }
);

export { axiosPrivate };
