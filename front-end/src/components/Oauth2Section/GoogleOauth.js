import React from "react";
import { GoogleLogin } from "@react-oauth/google";
const GoogleOauth = () => {
  const handleOauthResponse = async (response) => {
    console.log(response)
  };
  return (
    
   

    <GoogleLogin
    
    size="large"

        onSuccess={(credentialResponse) => {
          handleOauthResponse(credentialResponse);
        }}
        onError={() => {
          console.log("Login Failed");
        }}
      />

     

  );
};

export default GoogleOauth;
