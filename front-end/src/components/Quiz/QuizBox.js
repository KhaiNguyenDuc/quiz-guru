import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React from "react";
import { Link } from "react-router-dom";
import {
  MULTIPLE_CHOICE_QUESTION,
  SINGLE_CHOICE_QUESTION,
} from "../../utils/Constant";
import QuizService from "../../services/QuizService"
const QuizBox = ({ quiz, setQuizList }) => {

  const handleDelete = async (e) => {
    e.preventDefault()
    const response = await QuizService.deleteQuizById(quiz?.id);
    if(response?.status !== 400){
      setQuizList(prev => prev.filter(prev => prev?.id !== quiz?.id))
    }
  }
  return (
    <div className="quiz-box">
      <div className="quiz-text">
        <h2 className="quiz-name">Mức độ: {quiz?.level}</h2>
        <span>Ngày tạo: {quiz?.lastModifiedDate}</span>
        <span>
          Loại:{" "}
          {quiz?.type === MULTIPLE_CHOICE_QUESTION
            ? "Nhiều lựa chọn"
            : quiz?.type === SINGLE_CHOICE_QUESTION
            ? "Một lựa chọn"
            : "Ngẫu nhiên"}
        </span>
        <span>Số câu hỏi: {quiz?.number}</span>
        <span>Ngôn ngữ: {quiz?.language}</span>
        <span>Thời gian: {quiz?.duration / 60} phút</span>
        <button className="btn btn-danger text-white my-2" onClick={(e) => handleDelete(e)}>
       
          <FontAwesomeIcon
            icon="trash"
            style={{  fontSize: "20px", marginRight: '10px' }}
          />
          Xóa
        </button>
        <Link className="quiz-link text-dark" to={`/quiz/${quiz.id}`}>
          <span className="text-dark">Làm bài</span>{" "}
          <FontAwesomeIcon icon="arrow-right" />
        </Link>
      </div>
    </div>
  );
};

export default QuizBox;
