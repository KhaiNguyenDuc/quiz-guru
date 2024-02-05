import React, { useState, useEffect } from 'react';
import { Navigate } from 'react-router-dom';
import UserService from '../services/UserService';
const ProtectedRoute = ({ children }) => {

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
          if (userRoles.includes("ADMIN") || userRoles.includes("USER")) {
            setRole(userRoles)
          }
        };
  
        fetchUserInfo();
      }
    }, []);
  
    if (localStorage.getItem("accessToken") === null) {
      return <Navigate to="/auth/login" />;
    }
  
    if (role) {
      return children
    } else {
    <Navigate to="/auth/login" />;
    }

};

export default ProtectedRoute;
