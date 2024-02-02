import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import "./QuizList.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import UserService from "../../services/UserService";
import QuizBox from "./QuizBox";
import CustomPagination from "../Pagination/CustomPagination";
import PreLoader from "../PreLoader/PreLoader";
function QuizList() {
  const [quizList, setQuizList] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPage, setTotalPage] = useState();
  const [totalProduct, setTotalProduct] = useState();
  const [isLoading, setLoading] = useState(false);
  const nextPage = () => {
    setCurrentPage(currentPage + 1);
  };

  const prevPage = () => {
    setCurrentPage(currentPage - 1);
  };
  const getAllQuiz = async () => {
    setLoading(true);
    const response = await UserService.getCurrentUserQuiz(currentPage);
    if (response?.status !== 400) {
      setQuizList(response?.data);
      setTotalPage(response?.totalPages);
      setTotalProduct(response?.totalElements);
    }
    setLoading(false);
  };
  useEffect(() => {
    getAllQuiz();
  }, [currentPage]);

  const quizReturn = () => {
    return quizList.map((quiz) => <QuizBox quiz={quiz} setQuizList={setQuizList} />);
  };
  return (
    <>
      {" "}
      {isLoading ? (
        <>
          <PreLoader color={"white"} />
        </>
      ) : (
        <>
          <div>


            <div className="quiz-boxes history">{quizReturn()}</div>
            <div className="custom-pagination">
            <CustomPagination
              currentPage={currentPage}
              totalPage={totalPage}
              prevPage={prevPage}
              nextPage={nextPage}
              setCurrentPage={setCurrentPage}
              totalProduct={totalProduct}
            />
            </div>
          </div>
        </>
      )}
    </>
  );
}

export default QuizList;
