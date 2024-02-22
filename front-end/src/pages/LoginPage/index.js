import React, { useEffect, useState } from "react";
import "./index.css";
import PreLoader from "../../components/PreLoader/PreLoader";
import AuthService from "../../services/AuthService";
import UserService from "../../services/UserService";
import { Link, useNavigate } from "react-router-dom";
import { INVALID_LOGIN_MSG } from "../../utils/Constant";
import { useLocation } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import useUser from "../../hook/useUser";
import Oauth from "../../components/Oauth2Section/Oauth";
function LoginPage() {
  const location = useLocation();
  const { user, setUser } = useUser();
  const isCreated = location.state?.isCreated;
  const isVerify = location.state?.isVerify;
  const isReset = location.state?.isReset;
  const createdUsername = location.state?.username;
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isError, setError] = useState("");
  const navigate = useNavigate();
  const [isLoading, setLoading] = useState(false);

  useEffect(() => {
    if (createdUsername !== undefined) {
      setUsername(createdUsername);
    }
  }, []);
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    const response = await AuthService.login(username, password);
    if (response?.status === 403) {
      setLoading(false);

      navigate("/auth/verify", {
        state: {
          username: username,
          password: password,
          isLogin: true,
        },
      });
    } else if (response?.status !== 401) {
      localStorage.setItem("accessToken", response.accessToken);
      localStorage.setItem("refreshToken", response.refreshToken);
      setUser({
        ...user,
        roles: response?.user?.roles?.map((role) => role?.name),
      });
      setLoading(false);
      navigate("/normal/create/text");
    } else {
      setLoading(false);
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

              {isVerify && (
                <div className="success-section">
                  Xác thực tài khoản thành công
                </div>
              )}

              {isReset && (
                <div className="success-section">
                  Đặt lại mật khẩu thành công
                </div>
              )}

              <Oauth />
            </div>

            <div className="card-body">
              <form onSubmit={(e) => handleSubmit(e)} className="my-3">
                {isLoading && <PreLoader type={"spin"} color={"#FFFFFF"} />}

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
                    placeholder="Tên đăng nhập hoặc email"
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
                {isError && (
                  <div className="text-white my-3 error-section">{isError}</div>
                )}
                <div className="my-3 success-section">Username: khainguyen ; Password: k989898k</div>
                <div className="form-group">
                  <button className="btn login_btn mt-3">Đăng nhập</button>
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

export default LoginPage;
