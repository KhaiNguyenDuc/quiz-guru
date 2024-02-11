import React from "react";
import { NavLink } from "react-router-dom";
import "./OptionTab.css";
import { Nav } from "react-bootstrap";
const OptionTab = ({ options }) => {
  return (
    <Nav className="nav nav-tabs options">
      {options.map((option, index) => {
        return (
          <li className="nav-item" key={index}>
            <NavLink className="nav-link " to={option?.url} >
              {option?.text}
            </NavLink>
          </li>
        );
      })}
    </Nav>
  );
};

export default OptionTab;
