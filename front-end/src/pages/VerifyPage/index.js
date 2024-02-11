import React, { useEffect, useState } from "react";
import "./index.css";
import PreLoader from "../../components/PreLoader/PreLoader";
import AuthService from "../../services/AuthService";
import { Link, useNavigate } from "react-router-dom";
import { EMPTY_TOKEN_MSG, INVALID_LOGIN_MSG, NOT_FOUND_EMAIL, TOKEN_INVALID } from "../../utils/Constant";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Oauth from "../../components/Oauth2Section/Oauth";
import { useLocation } from "react-router-dom";
import useUser from "../../hook/useUser";

const VerifyPage = () => {
  let {user, setUser} = useUser();
  const location = useLocation();
  const isLogin = location.state?.isLogin;
  const password = location.state?.password;
  const username = location.state?.username;
  const [token, setToken] = useState("");
  const [isError, setError] = useState("");
  const [isResend, setResend] = useState(false);
  const navigate = useNavigate();
  const [isLoading, setLoading] = useState(false);
  const [resendDisabled, setResendDisabled] = useState(false);
  const [countdown, setCountdown] = useState(60);

  useEffect(() => {
    if(username === undefined){
      navigate("/auth/login")
    }
  }, []);

  useEffect(() => {
    let timer;
    if (resendDisabled) {
      timer = setInterval(() => {
        setCountdown((prevCount) => prevCount - 1);
      }, 1000);
    }
    return () => clearInterval(timer);
  }, [resendDisabled]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if(token === ""){
      setError(EMPTY_TOKEN_MSG);
      return;
    }
    setLoading(true);
    const response = await AuthService.verify(username, token);
    if (response?.status === 400) {
      setLoading(false);
      setError(TOKEN_INVALID);
    } else {
      setLoading(false);
      if(isLogin && password) {
        const response = await AuthService.login(username, password);
        if (response?.status !== 401) {
          localStorage.setItem("accessToken", response.accessToken);
          localStorage.setItem("refreshToken", response.refreshToken);
          setUser({...user, roles: response?.user?.roles?.map(role => role?.name)});
          navigate("/");
        } else {
          setError(INVALID_LOGIN_MSG);
        }
      } else {
        navigate("/auth/login", {
          state: {
            isVerify: true,
            username: username
          },
        });
      }
    }
  };

  const handleResendVerify = async () => {
    setLoading(true);
    setResendDisabled(true);
    const response = await AuthService.sendVerify(username);
    if(response?.status !== 400){
      setLoading(false);
      setResend(true);
      setTimeout(() => {
        setResendDisabled(false);
        setCountdown(60);
      }, 60000);
    }
  };

  return (
    <>
      <div className="my-5 find-account-page">
        <div className="d-flex justify-content-center ">
          <div className="card">
            <div className="card-header mt-3">
              <h3 className="text-white">Xác thực tài khoản</h3>
              {isResend ? (
                <div className="success-section">Chúng tôi đã gửi mã xác thức đến email với tài khoản: {username}</div>
              ) : (
                <div className="success-section">Hãy nhập mã xác thực đã được gửi đến email với tài khoản: {username}. Nếu không tìm thấy hãy kiểm tra hộp thư rác hoặc bấm "gửi lại"</div>
              )}
              <Oauth/>
            </div>
            <div className="card-body">
              <form onSubmit={(e) => handleSubmit(e)}>
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
                    name="token"
                    value={token}
                    onChange={(e) => {
                      setToken(e.target.value);
                      setError("");
                    }}
                    placeholder="Mã xác thực"
                  />
                </div>
                {isError && (
                  <div className="text-white my-3 error-section">{isError}</div>
                )}
                <div className="form-group">
                  <button className="btn find_btn">Xác thực tài khoản</button>
                </div>
              </form>
              <div className="form-group mt-2">
                <button disabled={resendDisabled} className="btn btn-success" onClick={() => handleResendVerify()} style={{width: '100%'}}>
                  {resendDisabled ? `Gửi lại mã xác thực (${countdown}s)` : "Gửi lại mã xác thực"}
                </button>
              </div>
            </div>
            <div className="card-footer">
              <div className="d-flex justify-content-center links">
                Chưa có tài khoản?. <Link to={"/auth/register"}>Đăng ký</Link>
                
              </div>
              <div className="d-flex justify-content-center links">
                Đã có tài khoản?. <Link to={"/auth/login"}>Đăng nhập tại đây</Link>
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
};

export default VerifyPage;
