import { axiosPrivate } from "../api";
import { WORDSET_URL } from "../utils/Constant";

class WordSetService {
    createWordSet(wordSet){
        return axiosPrivate.post(WORDSET_URL, wordSet)
        .then(response => response?.data)
        .catch(error => error?.data)
    }

    updateWordSet(wordSet){
        return axiosPrivate.put(WORDSET_URL + `/${wordSet?.id}`, {
            name: wordSet?.name
        })
        .then(response => response?.data)
        .catch(error => error?.data)
    }

    bindQuiz(bindObject){
        return axiosPrivate.post(WORDSET_URL + "/bind", bindObject)
        .then(response => response?.data)
        .catch(error => error?.data)
    }

    findWordsByWordSet(wordSetId, page){
        let URL = ""
        if(page !== undefined){
            URL = WORDSET_URL + `/${wordSetId}?page=${page}&size=6`
        }else{
            URL = WORDSET_URL + `/${wordSetId}?page=0&size=100`
        }
        return axiosPrivate.get(URL)
        .then(response => response?.data)
        .catch(error => error?.data)
    }

    deleteById(wordSetId){
        return axiosPrivate.delete(WORDSET_URL + `/${wordSetId}/delete`)
        .then(response => response?.data)
        .catch(error => error?.data)
    }
   
}
export default new WordSetService();