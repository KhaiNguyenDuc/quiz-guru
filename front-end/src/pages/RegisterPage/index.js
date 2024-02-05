import { Link } from "react-router-dom";
import "./index.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useState } from "react";
import {
  BLANK_LOGIN_MSG,
  EXIST_USERNAME_MSG,
  PASSWORD_MISSMATCH_MSG,
} from "../../utils/Constant";
import AuthService from "../../services/AuthService";
import { useNavigate } from "react-router-dom";
import PreLoader from "../../components/PreLoader/PreLoader";
import Oauth from "../../components/Oauth2Section/Oauth";
function RegisterPage() {
  const navigate = useNavigate();
  const [registerInfo, setRegisterInfo] = useState({});
  const [confirmPassword, setConFirmPassword] = useState("");
  const [isError, setError] = useState("");
  const [isLoading, setLoading] = useState(false);
  const handleChange = (e) => {
    setRegisterInfo({ ...registerInfo, [e.target.name]: e.target.value });
  };
  const handleRegister = async (e) => {
    e.preventDefault();
    setLoading(true);
    if (registerInfo?.username === "" || registerInfo?.password === "") {
      setError(BLANK_LOGIN_MSG);
      setLoading(false);
      return;
    } else if (registerInfo?.password !== confirmPassword) {
      setError(PASSWORD_MISSMATCH_MSG);
      setLoading(false);
      return;
    }
    const response = await AuthService.register(registerInfo);
    if (response?.status === 201) {
      setLoading(false);
      navigate("/auth/login", {
        state: {
          isCreated: true,
          username: response?.data?.data?.username,
        },
      });
    } else if (response?.status === 400) {
      setLoading(false);
      setError(EXIST_USERNAME_MSG);
    }
  };

  return (
    <>
      <div className=" my-5 register-page">
        <div className="d-flex justify-content-center ">
          <div className="card">
            <div className="card-header">
              <h3 className="text-white">Đăng ký</h3>
              <Oauth/>
            </div>
            <div className="card-body">
              <form onSubmit={(e) => handleRegister(e)}>
                {isLoading && <PreLoader color={"#ffffff"} type={"spin"} />}
                <div className="input-group mb-3">
                  <div className="input-group-prepend">
                    <span className="input-group-text">
                      <FontAwesomeIcon icon="user" />
                    </span>
                  </div>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Tên đăng nhập"
                    name="username"
                    value={registerInfo?.username}
                    onChange={(e) => {
                      handleChange(e);
                      setError("");
                    }}
                  />
                </div>
                <div className="input-group mb-3">
                  <div className="input-group-prepend">
                    <span className="input-group-text">
                      <FontAwesomeIcon icon="key" />
                    </span>
                  </div>
                  <input
                    type="password"
                    className="form-control"
                    placeholder="Mật khẩu"
                    name="password"
                    value={registerInfo?.password}
                    onChange={(e) => {
                      handleChange(e);
                      setError("");
                    }}
                  />
                </div>
                <div className="input-group mb-3">
                  <div className="input-group-prepend">
                    <span className="input-group-text">
                      <FontAwesomeIcon icon="key" />
                    </span>
                  </div>
                  <input
                    type="password"
                    className="form-control"
                    placeholder="Nhập lại mật khẩu"
                    name="confirmPassword"
                    value={confirmPassword}
                    onChange={(e) => {
                      setConFirmPassword(e.target.value);
                      setError("");
                    }}
                  />
                </div>
                {isError && (
                  <>
                    <div className="error-section my-3">{isError}</div>
                  </>
                )}

                <div className="form-group">
                  <button className="btn register_btn">Đăng ký</button>
                </div>
              </form>
            </div>
            <div className="card-footer">
              <div className="d-flex justify-content-center links">
                Đã tài khoản?. <Link to={"/auth/login"}>Đăng nhập</Link>
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

export default RegisterPage;
