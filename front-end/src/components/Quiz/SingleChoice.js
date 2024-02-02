import React from "react";

const SingleChoice = ({
  question,
  currentQuestion,
  selectAnswer,
  selectedIndex,
}) => {
  return (
    <>
      <div className="question-box">
        <div className="question-text">
          <h2 className="question-title">Câu hỏi: {currentQuestion + 1}</h2>
          <h3 className="question">{question?.query}</h3>
        </div>
      </div>
      <div className="answers-boxes">
        {question?.choices.map((choice, index) => {
          return (
            <label
              onClick={() => selectAnswer(index)}
              key={index}
              htmlFor={index}
              className={
                index === selectedIndex[selectedIndex.length -1]
                  ? "answer-label selected"
                  : "answer-label"
              }
            >
              {choice.name}
              <input type="radio" name="answer" id={index} />
            </label>
          );
        })}
      </div>
    </>
  );
};

export default SingleChoice;
