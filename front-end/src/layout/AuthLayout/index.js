import React from "react";
import { Outlet } from "react-router-dom";
import Footer from "../../components/Footer/Footer";

const AuthLayout = () => {
  return (
    <>
    <div className="container">

      <Outlet />
      
    </div>
    <Footer />
    </>
  );
};

export default AuthLayout;
