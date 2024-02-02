import React, { useState, useEffect } from 'react';
import { Navigate } from 'react-router-dom';

import UserService from '../services/UserService';

const RejectRoute = ({ children }) => {

  const [role, setRole] = useState(null);
  const getCurrentUserInfo = async () => {
    return await UserService.getCurrentUser()
  }
  useEffect(() => {
    if (localStorage.getItem("accessToken") !== null) {
      const fetchUserInfo = async () => {
        const response = await getCurrentUserInfo();
        if(response?.status === 401){
            return children
        }
        const userRoles = response.roles.map(role => role.name);
        if (userRoles.includes("ADMIN")) {
          setRole("ADMIN");
        } else if (userRoles.includes("USER")) {
          setRole("USER");
        }
      };

      fetchUserInfo();
    }
  }, []);

  if (localStorage.getItem("accessToken") === null) {
    return children;
  }

  if (role === "ADMIN") {
    return <Navigate to="/admin" />;
  } else if (role === "USER") {
    return <Navigate to="/" />;
  } else {
    return children;
  }
};

export default RejectRoute;
