import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import "./WordSet.css";
import { useNavigate } from "react-router-dom";
import { EMPTY_NAME } from "../../utils/Constant";
const WordSetCard = ({ wordSet, handleDelete }) => {
  const navigate = useNavigate();
  const viewWordSet = () => {
    navigate(`/member/word-set/${wordSet?.id}`)
  }


  
  return (
    <div className="word-set card" >
      <div className="card-body d-flex align-items-center justify-content-center text-center" onClick={() => viewWordSet()}>
        <div>
          <h5 className="card-title">{wordSet?.name === "" ? EMPTY_NAME : wordSet?.name}</h5>
          <FontAwesomeIcon icon={"clone"} /> {wordSet?.wordNumber} từ
          <FontAwesomeIcon icon={"pencil"} style={{ marginLeft: "20px" }} /> Đã
          ôn {wordSet?.reviewNumber} lần
        </div>
        
      </div>
 
      <div className="remove-btn" onClick={() => handleDelete()}>
      <FontAwesomeIcon icon={"trash-can"} className="btn"/>
      </div>
    </div>
  );
};

export default WordSetCard;
