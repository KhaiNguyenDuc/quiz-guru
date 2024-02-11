import React, { useEffect, useState } from "react";
import "./index.css";
import PreLoader from "../../components/PreLoader/PreLoader";
import AuthService from "../../services/AuthService";
import { Link, useNavigate } from "react-router-dom";
import { EMPTY_PASSWORD_MSG, EMPTY_TOKEN_MSG, NOT_FOUND_EMAIL, PASSWORD_INVALID_MSG, PASSWORD_MISSMATCH_MSG, TOKEN_INVALID } from "../../utils/Constant";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useLocation } from "react-router-dom";
import Oauth from "../../components/Oauth2Section/Oauth";

function ResetPasswordPage() {
  const [resetInfo, setResetInfo] = useState();
  const location = useLocation();
  const email = location.state?.email;
  const [isError, setError] = useState("");
  const navigate = useNavigate();
  const [isLoading, setLoading] = useState(false);
  const [isResend, setResend] = useState(false);
  const [resendDisabled, setResendDisabled] = useState(false); // state to manage resend button disabled state
  const [countdown, setCountdown] = useState(60); // initial countdown value

  const handleChange = (e) => {
    setResetInfo({...resetInfo, [e.target.name]: e.target.value})
  }

  useEffect(() => {
    if(email === undefined){
      navigate("/auth/login")
    }
  }, [])

  useEffect(() => {
    if (resendDisabled) {
      const countdownTimer = setInterval(() => {
        setCountdown((prevCount) => prevCount - 1);
      }, 1000);

      return () => clearInterval(countdownTimer);
    }
  }, [resendDisabled]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true)
    if(resetInfo?.password !== resetInfo?.confirmPassword){
      setLoading(false)
      setError(PASSWORD_MISSMATCH_MSG)
      return
    } else if(resetInfo?.token === ""){
      setLoading(false)
      setError(EMPTY_TOKEN_MSG)
      return
    }else if(resetInfo?.password === ""){
      setLoading(false)
      setError(EMPTY_PASSWORD_MSG)
      return
    }else if(resetInfo?.password.length < 7){
      setLoading(false)
      setError(PASSWORD_INVALID_MSG)
      return
    }
    const response = await AuthService.resetPassword({...resetInfo, email: email});
    if (response?.status === 400) {
      setLoading(false);
      setError(TOKEN_INVALID);
    } else {
      setLoading(false);
      navigate("/auth/login", {
        state: {
          isReset: true,
          username: email
        }
      });
    }
  };

  const handleResendEmail = async () => {
    setLoading(true)
    setResendDisabled(true); // disable resend button
    const response = await AuthService.sendResetPassword(email);
    if(response?.status !== 400){
      setLoading(false)
      setResend(true)
      setTimeout(() => {
        setResendDisabled(false); // re-enable resend button after 60 seconds
        setCountdown(60); // reset countdown value
      }, 60000); // 60 seconds
    }
  }

  return (
    <>
      <div className=" my-5 reset-password-page">
        <div className="d-flex justify-content-center ">
          <div className="card">
            <div className="card-header">
              <h3 className="text-white">Đặt lại mật khẩu</h3>
              {isResend ? (
                <div className="text-white success-section">Đã gửi mã xác nhận qua email: {email}</div>
              ) : (
                <div className="text-white success-section">Chúng tôi đã gửi cho bạn mã xác thực để đặt lại mật khẩu qua email: {email}.  Nếu không tìm thấy hãy kiểm tra hộp thư rác hoặc bấm "gửi lại"</div>
              )}
              <Oauth/>
            </div>

            <div className="card-body">
              <form onSubmit={(e) => handleSubmit(e)} className="mb-3">
                {isLoading && <PreLoader type={"spin"} color={"#FFFFFF"} />}

                <div className="input-group mb-3">
                  <div className="input-group-prepend">
                    <span className="input-group-text">
                      <FontAwesomeIcon icon="puzzle-piece" />
                    </span>
                  </div>
                  <input
                    type="text"
                    className="form-control"
                    name="token"
                    value={resetInfo?.token}
                    onChange={(e) => {
                      handleChange(e)
                      setError("");
                    }}
                    placeholder="Mã xác nhận"
                  />
                </div>

                <div className="input-group mb-3">
                  <div className="input-group-prepend">
                    <span className="input-group-text">
                      <FontAwesomeIcon icon="lock" />
                    </span>
                  </div>
                  <input
                    type="password"
                    className="form-control"
                    name="password"
                    value={resetInfo?.password}
                    onChange={(e) => {
                      handleChange(e)
                      setError("");
                    }}
                    placeholder="Mật khẩu"
                  />
                </div>

                <div className="input-group mb-3">
                  <div className="input-group-prepend">
                    <span className="input-group-text">
                      <FontAwesomeIcon icon="lock" />
                    </span>
                  </div>
                  <input
                    type="password"
                    className="form-control"
                    name="confirmPassword"
                    value={resetInfo?.confirmPassword}
                    onChange={(e) => {
                      handleChange(e)
                      setError("");
                    }}
                    placeholder="Nhập lại mật khẩu"
                  />
                </div>

                {isError && (
                  <div className="text-white my-3 error-section">{isError}</div>
                )}
                <div className="form-group">
                  <button className="btn reset_password_btn">Đặt lại mật khẩu</button>
                </div>
                
              </form>
              <div >
                  <button disabled={resendDisabled} onClick={() => handleResendEmail()} className="btn btn-success" style={{width: '100%'}}>
                    {resendDisabled ? `Gửi lại mã xác nhận (${countdown}s)` : "Gửi lại mã xác nhận"}
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
}

export default ResetPasswordPage;
