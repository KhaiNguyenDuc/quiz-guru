import React, { useEffect, useState } from "react";
import "./index.css";
import PreLoader from "../../components/PreLoader/PreLoader"
import AuthService from "../../services/AuthService";
import { Link, useNavigate } from "react-router-dom";
import { EMPTY_EMAIL_MSG, EMPTY_NAME, NOT_FOUND_EMAIL } from "../../utils/Constant";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Oauth from "../../components/Oauth2Section/Oauth";
function FindAccountPage() {
  const [email, setEmail] = useState("");
  const [isError, setError] = useState("");
  const navigate = useNavigate();
  const [isLoading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true)
    if(email === ""){
      setLoading(false)
      setError(EMPTY_EMAIL_MSG);
      return
    }
    const response = await AuthService.sendResetPassword(email);
    if (response?.status === 400) {
     setLoading(false)
     setError(NOT_FOUND_EMAIL);
    }else{
        setLoading(false)
        navigate("/auth/reset-password", {
          state: {
            email: email
          },
        })
    }

  };
  return (
    <>
      
      <div className=" my-5 find-account-page">
       
        <div className="d-flex justify-content-center ">
          <div className="card">
            
            <div className="card-header mt-3">
              <h3 className="text-white">Tìm tài khoản</h3>
              <Oauth/>
            </div>
            
            <div className="card-body">
        
           
              <form onSubmit={(e) => handleSubmit(e)}>
                {isLoading && (<PreLoader type={"spin"} color={"#FFFFFF"}/>)}
              
                <div className="input-group mb-3">
                  <div className="input-group-prepend">
                    <span className="input-group-text">
                      <FontAwesomeIcon icon="envelope" />
                    </span>
                  </div>
                  <input
                    type="email"
                    className="form-control"
                    name="email"
                    value={email}
                    onChange={(e) => {
                      setEmail(e.target.value);
                      setError("");
                    }}
                    placeholder="Email"
                  />
                </div>

                {isError && (
                  <div className="text-white my-3 error-section">{isError}</div>
                )}
                <div className="form-group">
                  <button className="btn find_btn">Đặt lại mật khẩu</button>
                </div>
              </form>
            </div>
          
            <div className="card-footer">
              <div className="d-flex justify-content-center links">
                Chưa có tài khoản?. <Link to={"/auth/register"}>Đăng ký</Link>
              </div>
              <div className="d-flex justify-content-center">
                <Link to={"/auth/find-account"}>Quên mật khẩu?</Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default FindAccountPage;
