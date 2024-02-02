import { useContext } from 'react'
import QuizContext from '../context/QuizContext'
const useQuiz = () => {
  return useContext(QuizContext)
}

export default useQuiz
