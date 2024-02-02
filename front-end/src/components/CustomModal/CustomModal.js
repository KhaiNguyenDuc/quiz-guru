import { useState } from "react";
import Modal from "react-modal";
import "./CustomModal.css"
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
const CustomModal = ({ modalIsOpen, setIsOpen, width, children }) => {
  const closeModal = () => {
    setIsOpen(false);
  };
    // Extract numeric value from width string
    const numericWidth = parseInt(width);
  const customStyles = {
    content: {
      top: "50%",
      left: "50%",
      right: "auto",
      bottom: "auto",
      marginRight: "-60%",
      transform: "translate(-50%, -50%)",

      width: width ? width : '400px'
    },
  };
  return (
   
   <Modal
      isOpen={modalIsOpen}
      onRequestClose={closeModal}
      style={customStyles}
      contentLabel="Example Modal"
    >
    
      <button
        onClick={closeModal}
        className="btn btn-danger text-white"
        style={{ marginLeft: numericWidth ? `calc(${numericWidth-100}px)` : '300px'}}
      >
        <FontAwesomeIcon icon="close"/>
      </button>
    

      {children}

    </Modal>

 
  );
};

export default CustomModal;
