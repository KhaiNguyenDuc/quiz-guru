import React from "react";

const MultipleChoice = ({
  question,
  currentQuestion,
  selectAnswer,
  selectedIndex,
}) => {
  const handleSelect = (index) => {
   
      selectAnswer(index);
    
  };

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

              key={index}
              htmlFor={index}
              className={
                selectedIndex.includes(index)
                  ? "answer-label selected"
                  : "answer-label"
              }
            >
              {choice.name}
              <input
                type="checkbox"
                name="answer"
                id={index}
                checked={selectedIndex.includes(index)}
                onChange={() => handleSelect(index)}
              />
            </label>
          );
        })}
      </div>
    </>
  );
};

export default MultipleChoice;
