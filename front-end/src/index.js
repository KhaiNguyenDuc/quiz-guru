import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
import { BrowserRouter } from "react-router-dom";


import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min";

import "./utils/Fontawesome.js";
import { GivenTextProvider } from "./context/GivenTextContext.js";
import { UserProvider } from "./context/UserContext.js";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { GOOGLE_CLIENT_ID } from "./utils/Constant.js";
const root = ReactDOM.createRoot(document.getElementById("root"));

root.render(

    <BrowserRouter>
      <GoogleOAuthProvider clientId={GOOGLE_CLIENT_ID}>
        <GivenTextProvider>
          <UserProvider>
            <App />
          </UserProvider>
        </GivenTextProvider>
      </GoogleOAuthProvider>
    </BrowserRouter>

);
