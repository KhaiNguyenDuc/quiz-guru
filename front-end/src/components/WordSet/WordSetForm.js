import { useState } from "react";
import WordSetService from "../../services/WordSetService"
import { useNavigate } from "react-router-dom";
import { validateInput } from "../../utils/Utils";
import { GENERATE_LENGTH_INVALID } from "../../utils/Constant";
const WordSetForm = () => {
    const [wordSet, setWordSet] = useState();
    const navigate = useNavigate();
    const [isError, setError] = useState()
    const handleChange = (e) => {
        setWordSet({...wordSet, [e.target.name]: e.target.value})
    }
    const hanldeSubmit = async (e) => {
        e.preventDefault()

        if (
          wordSet?.name === undefined || wordSet?.words === undefined || ! validateInput(wordSet?.words)
          ) {
            setError(GENERATE_LENGTH_INVALID);
            return;
          }
      
        const vocabulary = wordSet?.words?.split(/[,\n]+/)
        const response = await WordSetService.createWordSet({...wordSet, words: vocabulary.map(voc => {
            return {
              name: voc
            }
          })});
        if(response?.status !==400){
            navigate(`/member/word-set/${response?.data}`, {
                state: {
                  quizId: wordSet?.quizId
                },
              })
        }
    }
  return (
    <form className="my-3" onSubmit={(e) => hanldeSubmit(e)}>
         {isError && <div className="error-section mb-3">{isError}</div>}
      <div className="mb-3">
        <label for="name">
          <h5>Tên danh sách: </h5>
        </label>

        <input
          id="name"
          type="text"
          className="form-control"
          name="name"
          placeholder="Tên danh sách"
        
          onChange={(e) => {handleChange(e); setError("")}}
          value={wordSet?.name}
        />
      </div>
      <div>
        <label for="words">
          <h5>Từ vựng </h5>
        </label>
        <textarea
          placeholder="Nhập danh sách từ vựng cần ôn tập, mỗi từ cách nhau bởi dấu ',' hoặc xuống dòng. Ví dụ: sleep, walk, study,... Hoặc
          Sleep
          Walk
          Study
          ..."
          className="form-control"
          rows={7}
          defaultValue={""}
          id="words"
          name="words"
          onChange={(e) => {handleChange(e); setError("")}}
          value={wordSet?.words}
        />
      </div>

      <div className="mt-3">
        <button className="btn btn-success" style={{ width: "100%" }}>
          Tạo
        </button>
      </div>
    </form>
  );
};

export default WordSetForm;
