import { axiosPrivate } from "../api"
import { WORD_URL } from "../utils/Constant"

class WordService {
    getDefinition(words, wordSetId){
        return axiosPrivate.post(WORD_URL + `/word-set/${wordSetId}`, words)
            .then(response => response?.data?.data)
            .catch(error => error?.data)
    }

    updateDefinition(id, htmlContent){
        return axiosPrivate.put(WORD_URL + `/${id}`, {
            content: htmlContent
        })
            .then(response => response?.data?.data)
            .catch(error => error?.data)
    }
}

export default new WordService()