import { axiosPrivate } from "../api";
import { USER_URL } from "../utils/Constant";

class UserService {
  getCurrentUser() {
    return axiosPrivate
      .get(USER_URL + "/current")
      .then((response) => response?.data?.data)
      .catch((error) => error?.data);
  }

  getCurrentUserWordSets(page, size) {
    let URL = "";
    if (size !== undefined && page !== undefined) {
      URL = USER_URL + `/current/word-set?page=${page}&size=${size}`;
    } else {
      URL = USER_URL + `/current/word-set?page=0&size=10`;
    }
    return axiosPrivate
      .get(URL)
      .then((response) => response?.data)
      .catch((error) => error?.data);
  }

  getCurrentUserQuiz(page) {
    return axiosPrivate
      .get(USER_URL + `/current/quizzes?page=${page}&&size=4`)
      .then((response) => response?.data)
      .catch((error) => error?.response);
  }

  getCurrentUserQuizById(id) {
    return axiosPrivate
      .get(USER_URL + `/current/quiz?id=${id}`)
      .then((response) => response?.data?.data)
      .catch((error) => error?.response);
  }

  getCurrentUserRecordById(id) {
    return axiosPrivate
      .get(USER_URL + `/current/record?id=${id}`)
      .then((response) => response?.data?.data)
      .catch((error) => error?.response);
  }

  getCurrentUserRecords(page) {
    return axiosPrivate
      .get(USER_URL + `/current/records?page=${page}&&size=5`)
      .then((response) => response?.data)
      .catch((error) => error?.response);
  }

  updateProfile(userProfile) {
    return axiosPrivate
      .put(USER_URL + `/current/update`, userProfile, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
      .then((response) => response?.data)
      .catch((error) => error?.response);
  }
}
export default new UserService();
