
import { axiosPrivate } from "../api/index.js"
import { QUIZ_URL } from "../utils/Constant.js";


class QuizService {


  generateQuizByText(textbase) {
    return axiosPrivate
      .post(QUIZ_URL + "/generate/text", textbase)
      .then((response) => response?.data?.data)
      .catch((error) => error?.response);
  }

  generateQuizByVocabulary(vocabularyBase) {
    return axiosPrivate
      .post(QUIZ_URL + "/generate/vocabulary", vocabularyBase)
      .then((response) => response?.data?.data)
      .catch((error) => error?.response);
  }

  generateQuizByTxt(filebase, file) {
    return axiosPrivate
      .post(QUIZ_URL + "/generate/txt", 
      {
        ...filebase,
        file: file
      },   
      {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
      .then((response) => response?.data?.data)
      .catch((error) => error?.response);
  }
  generateQuizByPdf(filebase, file) {
    return axiosPrivate
      .post(QUIZ_URL + "/generate/pdf", 
      {
        ...filebase,
        file: file
      },   
      {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
      .then((response) => response?.data?.data)
      .catch((error) => error?.response);
  }
  generateQuizByDoc(filebase, file) {
    return axiosPrivate
      .post(QUIZ_URL + "/generate/doc", 
      {
        ...filebase,
        file: file
      },   
      {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
      .then((response) => response?.data?.data)
      .catch((error) => error?.response);
  }

  deleteQuizById(quizId){
    return axiosPrivate
      .delete(QUIZ_URL + `/${quizId}/delete`)
      .then((response) => response?.data?.data)
      .catch((error) => error?.response);
  }

  generateQuizByTextToVocab(textToVocabBase){
    return axiosPrivate
      .post(QUIZ_URL + "/generate/text-to-vocab", textToVocabBase)
      .then((response) => response?.data?.data)
      .catch((error) => error?.response);
  }

  generateQuizVocabByTxt(fileVocabBase, file) {
    return axiosPrivate
      .post(QUIZ_URL + "/generate/txt-to-vocab", 
      {
        ...fileVocabBase,
        file: file
      },   
      {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
      .then((response) => response?.data?.data)
      .catch((error) => error?.response);
  }
  generateQuizVocabByPdf(fileVocabBase, file) {
    return axiosPrivate
      .post(QUIZ_URL + "/generate/pdf-to-vocab", 
      {
        ...fileVocabBase,
        file: file
      },   
      {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
      .then((response) => response?.data?.data)
      .catch((error) => error?.response);
  }
  generateQuizVocabByDoc(fileVocabBase, file) {
    return axiosPrivate
      .post(QUIZ_URL + "/generate/doc-to-vocab", 
      {
        ...fileVocabBase,
        file: file
      },   
      {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
      .then((response) => response?.data?.data)
      .catch((error) => error?.response);
  }



}

export default new QuizService();
