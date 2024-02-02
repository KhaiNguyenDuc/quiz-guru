import React, { useEffect, useState } from "react";
import "./index.css";
import PreLoader from "../../components/PreLoader/PreLoader"
import AuthService from "../../services/AuthService";
import { Link, useNavigate } from "react-router-dom";
import { INVALID_LOGIN_MSG } from "../../utils/Constant";
import { useLocation } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import GoogleOauth from "../../components/Oauth2Section/GoogleOauth";
function LoginPage() {
  const location = useLocation();
  const isCreated = location.state?.isCreated;
  const createdUsername = location.state?.username;
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isError, setError] = useState("");
  const navigate = useNavigate();
  const [isLoading, setLoading] = useState(false);
  useEffect(() => {
    if (isCreated && createdUsername !== undefined) {
      setUsername(createdUsername);
    }
  }, []);
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true)
    const response = await AuthService.login(username, password);
    if (response?.status !== 401) {
      localStorage.setItem("accessToken", response.accessToken);
      localStorage.setItem("refreshToken", response.refreshToken);
      setLoading(false)
      navigate("/")
    } else {
      setLoading(false)
      setError(INVALID_LOGIN_MSG);
    }
  };
  return (
    <>
      
      <div className=" my-5 login-page">
       
        <div className="d-flex justify-content-center ">
          <div className="card">
            
            <div className="card-header">
              <h3 className="text-white">Đăng nhập</h3>
              
              {isCreated && (
                <div className="success-section">Tạo tài khoản thành công</div>
              )}
              
              <div className="d-flex justify-content-end social_icon">
                <span>
                  <FontAwesomeIcon icon="fa-brands fa-square-facebook" />
                </span>
                <span>
                  <FontAwesomeIcon icon="fa-brands fa-google-plus-square" />
                </span>
                <span>
                  <FontAwesomeIcon icon="fa-brands fa-twitter-square" />
                </span>
              </div>
            </div>
            
            <div className="card-body">
            <div className="oauth-login">
            <GoogleOauth/>
            </div>
           
              <form onSubmit={(e) => handleSubmit(e)} className="my-3">
                {isLoading && (<PreLoader type={"spin"} color={"#FFFFFF"}/>)}
              
                <div className="input-group mb-3">
                  <div className="input-group-prepend">
                    <span className="input-group-text">
                      <FontAwesomeIcon icon="user" />
                    </span>
                  </div>
                  <input
                    type="text"
                    className="form-control"
                    name="username"
                    value={username}
                    onChange={(e) => {
                      setUsername(e.target.value);
                      setError("");
                    }}
                    placeholder="Tên đăng nhập"
                  />
                </div>
                <div className="input-group ">
                  <div className="input-group-prepend">
                    <span className="input-group-text">
                      <FontAwesomeIcon icon="key" />
                    </span>
                  </div>
                  <input
                    type="password"
                    className="form-control"
                    name="password"
                    value={password}
                    onChange={(e) => {
                      setPassword(e.target.value);
                      setError("");
                    }}
                    placeholder="Mật khẩu"
                  />
                </div>
                <div className="row align-items-center remember">
                  <input type="checkbox" />
                  Nhớ mật khẩu
                </div>
                {isError && (
                  <div className="text-white my-3 error-section">{isError}</div>
                )}
                <div className="form-group">
                  <button className="btn login_btn">Đăng nhập</button>
                </div>
              </form>
            </div>
            <div className="card-footer">
              <div className="d-flex justify-content-center links">
                Chưa có tài khoản?. <Link to={"/auth/register"}>Đăng ký</Link>
              </div>
              <div className="d-flex justify-content-center">
                <Link>Quên mật khẩu?</Link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default LoginPage;
