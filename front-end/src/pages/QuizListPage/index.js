import React from "react";
import QuizList from "../../components/Quiz/QuizList";
import IntroBox from "../../components/IntroBox/IntroBox";
import { INTRO_QUIZ } from "../../Data/Data";
function QuizListPage() {
  return (
    <>
      <IntroBox intro={INTRO_QUIZ}/>
      <QuizList />
    </>
  );
}

export default QuizListPage;
