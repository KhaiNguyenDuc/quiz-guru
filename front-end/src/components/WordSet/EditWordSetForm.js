import { useEffect, useState } from "react";
import WordSetService from "../../services/WordSetService";
import { TRY_AGAIN_MSG } from "../../utils/Constant";

const EditWordSetForm = ({defaultName, wordSetId, setIsEditOpen, setName}) => {

  const [wordSet, setWordSet] = useState({})
  const [error, setError] = useState()
  const handleSubmit = async (e) => {
    e.preventDefault()
    const response = await WordSetService.updateWordSet(wordSet);
    if(response?.status !== 400){
        setName(wordSet?.name)
        setIsEditOpen(false)
    }else{
        setError(TRY_AGAIN_MSG)
    }
  }

  useEffect(() => {
    setWordSet({...wordSet, name: defaultName, id: wordSetId})
  }, [])
  const handleChange = (e) => {
    setWordSet({...wordSet, [e.target.name]: e.target.value});
  }
  return (
    <form onSubmit={(e) => handleSubmit(e)}>
      <>
      {error && (
        <div className="error-section">{error}</div>
      )}
        <label htmlFor="text-content" className="form-label">
          <h5>Tên</h5>
        </label>
        <input
          placeholder="Tên danh sách"
          className="form-control"
          id="text-content"
          type="text"
          value={wordSet?.name}
          name="name"
          onChange={(e) => handleChange(e)}
        />
      </>

      <div className="text-end my-3">
        <button className="btn btn-primary">Lưu</button>
      </div>
    </form>
  );
};

export default EditWordSetForm;
