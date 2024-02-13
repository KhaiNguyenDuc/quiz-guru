import React, { useState, useEffect } from "react";
import "./index.css";
import { Link, NavLink, Outlet } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import useUser from "../../hook/useUser";
import UserService from "../../services/UserService";
import { useNavigate } from "react-router-dom";
import {
  EMPTY_NAME,
  TRY_AGAIN_MSG,
  USERNAME_EXIST,
} from "../../utils/Constant";
import { useLocation } from 'react-router-dom';
import PreLoader from "../PreLoader/PreLoader";
import { Nav } from "react-bootstrap";
const SideBar = ({ children }) => {
  const { pathname } = useLocation();
  const navigate = useNavigate();
  const [selectedFile, setFile] = useState();
  const [imageUrl, setImageUrl] = useState();
  const [isLoading, setLoading] = useState(false);
  const [isError, setError] = useState("");
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const { user, setUser } = useUser();
  const [username, setUsername] = useState("");
  const handleFileChange = async (e) => {
    const file = e.target.files[0];
    setFile(file);
    const imageUrl = URL.createObjectURL(e.target.files[0]);

    const response = await UserService.updateProfile({
      ...user,
      file: file,
    });
    if (response?.status !== 400) {
      setImageUrl(imageUrl);
      setUser({ ...user, imagePath: selectedFile });
      setEditMode(false);
    } else {
      setError(TRY_AGAIN_MSG);
    }
  };
  const toggleSidebar = () => {
    setSidebarOpen(!sidebarOpen);
  };
  const getUserInfo = async () => {
    setLoading(true);
    const response = await UserService.getCurrentUser();
    if (response?.status == 400) {
      return;
    }
    setUser(response);
    setUsername(response?.username);
    setLoading(false);
  };
  const [editMode, setEditMode] = useState(false);

  useEffect(() => {
   
      getUserInfo();
    
  }, []);

  const handleLogout = (e) => {
    e.preventDefault();
    setUser({});
    localStorage.clear();
    navigate("/auth/login");
  };

  const handleEditClick = () => {
    setEditMode(true);
  };

  const handleChange = (e) => {
    setUsername(e.target.value);
  };

  const handleSubmit = async (e) => {
    if (e !== "") {
      e.preventDefault();
    }

    if (username.trim() === "") {
      setError(EMPTY_NAME);
      setUsername(user.username);
      return;
    }
    const response = await UserService.updateProfile({
      ...user,
      username: username,
    });
    if (response?.status !== 400) {
      setUser({ ...user, username: username });
      setEditMode(false);
    } else {
      setError(USERNAME_EXIST);
    }
  };
  return (
    <>
      {isLoading ? (
        <PreLoader color={"black"} type={"bars"} />
      ) : (
        <Nav className="wrapper d-flex align-items-stretch sidebar-wrap ">
          <div
            id="sidebar"
            className={`${sidebarOpen ? "active" : ""}  sticky-top`}
          >
            <div className="custom-menu">
              <button
                className="btn btn-primary"
                onClick={() => toggleSidebar()}
              >
                <FontAwesomeIcon
                  icon={sidebarOpen ? "arrow-right" : "arrow-left"}
                />
              </button>
            </div>{" "}
            <div className="bg-wrap text-center py-4 side-img img">
              {user && (
                <>
                  <form className="user-logo" onSubmit={(e) => handleSubmit(e)}>
                    <div className="user-info">
                      <div className="user-avatar">
                        <label>
                          <div className="edit-avatar">
                            <FontAwesomeIcon icon="file-upload" />
                          </div>
                          <input
                            type="file"
                            className="account-settings-file"
                            style={{ display: "none" }}
                            accept=".png, .jpg"
                            onChange={(e) => {
                              handleFileChange(e);
                            }}
                          />
                        </label>
                        {user.imagePath === null && imageUrl === undefined ? (
                          <div className="img" />
                        ) : (
                          <img
                            src={user?.imagePath ? user.imagePath : imageUrl}
                            alt=""
                            className="img"
                          />
                        )}
                      </div>
                    </div>

                    {isError && (
                      <div className="error-section my-3">{isError}</div>
                    )}
                    {!editMode ? (
                      <>
                        <h3>
                          {username}
                          <FontAwesomeIcon
                            className="edit-username"
                            icon="pencil"
                            style={{
                              color: "white",
                              marginLeft: "10px",
                              cursor: "pointer",
                            }}
                            onClick={handleEditClick}
                          />
                        </h3>
                      </>
                    ) : (
                      <div className="d-flex justify-content-center">
                        <input
                          style={{ width: "30%" }}
                          type="text"
                          className="text-center form-control d-inline"
                          value={username}
                          name="username"
                          onChange={(e) => {
                            handleChange(e);
                            setError("");
                          }}
                        />
                        <button className="btn btn-primary ms-2">Lưu</button>
                      </div>
                    )}
                  </form>
                </>
              )}
            </div>
            <ul className="list-unstyled text-start  mb-5 ">
              <li>
                <>
                  {user === undefined && (
                    <>
                      <NavLink to={"/auth/login"}>
                        <FontAwesomeIcon icon="home" className="fa-icon" />{" "}
                        <span>Đăng nhập</span>
                      </NavLink>
                    </>
                  )}
                </>
              </li>
              {user !== undefined && (
                <>
                  <li>
                    <NavLink to={"/normal/create/text"}
                    className={ ['/normal/create/text', '/normal/create/file'].includes(pathname) ? 'active': ''}
                    
                   >
                      <FontAwesomeIcon icon="home" className="fa-icon" />{" "}
                      <span>Tạo câu hỏi</span>
                    </NavLink>
             
                  </li>
                  <li>
                    <NavLink to={"/member/vocabulary/create/text"}
                    className={ ['/member/vocabulary/create/text-to-vocab', '/member/vocabulary/create/file-to-vocab'].includes(pathname) ? 'active': ''}
                    >
                     <FontAwesomeIcon icon={"clone"} className="fa-icon" /> Học
                      từ vựng
                    </NavLink>
                  </li>
                  <li>
                    <NavLink to={"/member/quiz"}>
                      <FontAwesomeIcon icon={"pencil"} className="fa-icon" />{" "}
                      Luyện tập
                    </NavLink>
                  </li>
                  <li>
                    <NavLink to={"/member/library"}>
                      <FontAwesomeIcon icon={"bank"} className="fa-icon" /> Kho
                      từ vựng
                    </NavLink>
                  </li>

                  <li>
                    <NavLink to={"/member/record"}
                    >
                      <FontAwesomeIcon
                        icon={"note-sticky"}
                        className="fa-icon"
                      />{" "}
                      Lịch sử làm bài
                    </NavLink>
                  </li>

                  <li>
                    <Link onClick={(e) => handleLogout(e)}>
                      <span className="pr-3">
                        <FontAwesomeIcon
                          icon={"sign-out"}
                          className="fa-icon"
                        />
                      </span>
                      Đăng xuất
                    </Link>
                  </li>
                </>
              )}
            </ul>
          </div>
          <div id="content" className="p-4 p-md-5 pt-5 text-start">
            <Outlet />
          </div>
        </Nav>
      )}
    </>
  );
};

export default SideBar;
