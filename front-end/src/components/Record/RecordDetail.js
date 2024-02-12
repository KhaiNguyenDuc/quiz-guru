import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import "./RecordDetail.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
const RecordDetail = ({ record }) => {
  const [allAnswers, setAllAnswers] = useState([]);
  const [allQuestions, setAllQuestions] = useState([]);
  useEffect(() => {
    if (record?.id) {
      setAllQuestions(
        record?.recordItems.map((item) => {
          return {
            id: item?.question?.id,
            query: item?.question?.query,
            choices: item?.question?.choices,
            explanation: item?.question?.explanation,
          };
        })
      );
      setAllAnswers(
        record?.recordItems.map((item) => {
          return {
            id: item?.selectedChoices.map((choice) => choice.id),
            name: item?.selectedChoices.map((choice) => choice.name),
            isCorrect: item?.selectedChoices.map((choice) => choice.isCorrect),
            explanation: item?.question?.explanation,
          };
        })
      );
    }
  }, [record]);

  return (
    <div className="result">
      <div className="result-box">
        <div className="result-bg">
          <span className="percentile">
            {Math.round((record?.score / allQuestions.length) * 100)}%
          </span>
         
        </div>
        <p className="result-detail">
          Bạn trả lời đúng {record?.score} trên {allQuestions.length} tổng câu
          hỏi
        </p>
        <Link to="/normal/create/text" className="new-quiz">
          Bắt đầu bộ câu hỏi mới!
        </Link>
      </div>
      <div className="row">
      {record?.givenText !== "" ? (
              <div className={`col scrollable-col `}>
                <div
                  className="result given-text"
                  style={{
                    maxWidth: "100%",
                    maxHeight: "100%",
                    overflow: "auto",
                  }}
                >
                  <p
                    style={{
                      maxWidth: "100%",
                      maxHeight: "100%",
                      overflow: "auto",
                    }}
                    dangerouslySetInnerHTML={{ __html: record?.givenText }}
                  />
                </div>
              </div>
            ) : (
              <></>
            )}

        <div className="col-12 col-md col-lg">
          <div className="check-answers-boxes">
            {allQuestions.map((item, key) => {
              return (
                <div
                  key={key}
                  className={
                    allAnswers[key]?.isCorrect.includes(false) ||
                    allAnswers[key]?.isCorrect.length === 0
                      ? "check-answer-box wrong"
                      : "check-answer-box correct"
                  }
                >
                  <div className="check-answer-top">
                    <div className="check-texts">
                      <p className="check-answer-count">Câu hỏi: {key + 1}</p>
                      <h3 className="check-answer-question">{item.query}</h3>
                    </div>
                    <div className="check-icon">
                      {allAnswers[key]?.isCorrect.includes(false) ||
                      allAnswers[key]?.isCorrect.length === 0 ? (
                        <FontAwesomeIcon icon="x" />
                      ) : (
                        <FontAwesomeIcon icon="check" />
                      )}
                    </div>
                  </div>
                  <div className="check-answer-bottom">
                    <div className="answer-box">
                      <span className="answer-title">Câu trả lời của bạn</span>
                      {allAnswers[key].name.length === 0 ? (
                        <span className="answer-text">Bỏ qua</span>
                      ) : (
                        <>
                          {allAnswers[key]?.name.map((n, index) => (
                            <span className="answer-text">
                              ({index + 1}). {n}
                            </span>
                          ))}
                        </>
                      )}
                    </div>
                    <div className="answer-box">
                      <span className="answer-title">Đáp án đúng</span>

                      {item.choices.map((answer, index) => {
                        return (
                          <span className="answer-text">
                            {answer.isCorrect ? answer.name : null}
                          </span>
                        );
                      })}
                    </div>
                  </div>
                  <div className="mt-3">
                    <p>Giải thích:</p>
                    <span className="answer-text">
                      {allAnswers[key]?.explanation}
                    </span>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>
    </div>
  );
};

export default RecordDetail;
