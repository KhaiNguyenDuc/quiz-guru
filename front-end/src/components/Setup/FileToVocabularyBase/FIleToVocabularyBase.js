import "./FileToVocabularyBase.css";
import { useEffect, useState } from "react";
import QuizService from "../../../services/QuizService";
import { useNavigate } from "react-router-dom";
import {
  ALLOWED_TYPE,
  DOCX_TYPE,
  EMPTY_FILE,
  PDF_TYPE,
  TRY_AGAIN_MSG,
  TXT_TYPE,
  UNSUPPORT_MEDIA_TYPE_MSG,
} from "../../../utils/Constant";
import PreLoader from "../../PreLoader/PreLoader";
import UserService from "../../../services/UserService";
const FileToVocabularyBase = () => {
  const navigate = useNavigate();
  const [isError, setError] = useState("")
  const [isLoading, setLoading] = useState(false)
  const [isCreatedSet, setCreatedSet] = useState(true);
  const [wordSets, setWordSets] = useState([]);
  const [fileToVocab, setfileToVocab] = useState({
    type: 3,
    number: 5,
    language: "english",
    level: 1,
    duration: 10,
    isDoQuiz: true,
    numberOfWords: 5,
    wordSetId: "",
    wordSetName: "",
  });
  const [selectedFile, setFile] = useState();
  const handleChange = (e) => {
    setfileToVocab({ ...fileToVocab, [e.target.name]: e.target.value });
  };
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true)
    let response = {};
    if(selectedFile === undefined){
      setError(EMPTY_FILE)
      return;
    }
    // Convert to second
    const payload = {...fileToVocab, duration: fileToVocab.duration* 60, isDoQuiz: fileToVocab.isDoQuiz,
        wordSetId: (!isCreatedSet && fileToVocab.wordSetId === "") ? wordSets[0]?.id : fileToVocab.wordSetId}
    if (selectedFile.type === TXT_TYPE) {
      response = await QuizService.generateQuizVocabByTxt(payload, selectedFile );
    } else if (selectedFile.type === PDF_TYPE) {
      response = await QuizService.generateQuizVocabByPdf(payload, selectedFile);
    } else if (selectedFile.type === DOCX_TYPE) {
      response = await QuizService.generateQuizVocabByDoc(payload, selectedFile);
    } else {
      setLoading(false)
      setError(UNSUPPORT_MEDIA_TYPE_MSG)
      return;
    }

    if (response?.status !== 400 && response?.status !== 415) {
      setLoading(false)
      navigate(`/quiz/${response}`);
    }else {
      setLoading(false)
      setError(TRY_AGAIN_MSG)
      return
    }
   
  };
  const getAllCurrentUserWordSets = async () => {
    const response = await UserService.getCurrentUserWordSets();
    if (response?.status !== 400) {
      setWordSets(response?.data);
    }
  };
  useEffect(() => {
    getAllCurrentUserWordSets();
  }, []);

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file && ALLOWED_TYPE.includes(file.type)) {
      setError("")
      setFile(file);
    } else {
      setError(UNSUPPORT_MEDIA_TYPE_MSG)
      e.target.value = "";
    }

  };
  return (
    <>
      <form className="mt-5" onSubmit={(e) => handleSubmit(e)}>
        {isLoading && (<PreLoader color={"black"} type={"bars"} message={"Đang khởi tạo bộ câu hỏi"}/>)}
        {isError && (<div className="error-section">{isError}</div>)}
        <div className="row my-5 mx-2"> 
        
          <label htmlFor="formFile" className=" form-label text-content">
            Chọn file để tạo câu hỏi
          </label>
          <input
            className="form-control"
            type="file"
            id="formFile"
            accept=".txt, .pdf, .docx"
            onChange={(e) => {handleFileChange(e)}}
          />
        </div>

        <div className="row my-5 mx-2">
          <div className="col">
            <h5>Loại</h5>
            <select
              className="form-control "
              id="inputGroupSelect01"
              name="type"
              onChange={(e) => handleChange(e)}
              value={fileToVocab?.type}
            >
              <option value={1}>Một đáp án</option>
              <option value={2}>Nhiều đáp án</option>
              <option value={3}>Ngẫu nhiên</option>
            </select>
          </div>
          <div className="col">
            <h5>Số lượng</h5>
            <select
              className="form-control "
              id="inputGroupSelect01"
              name="number"
              onChange={(e) => handleChange(e)}
              value={fileToVocab?.number}
            >
              <option value={5}>5 Câu hỏi</option>
              <option value={10}>10 Câu hỏi</option>
            </select>
          </div>
        </div>
        <div className="row mt-5 mb-4 mx-2">
          <div className="col-4">
            <h5>Số từ</h5>

            <select
              className="form-control "
              id="inputGroupSelect01"
              name="numberOfWords"
              onChange={(e) => handleChange(e)}
              value={fileToVocab?.numberOfWords}
            >
              <option value={5}>5 từ</option>
              <option value={10}>10 từ</option>
            </select>
          </div>
          <div className="col">
            <h5>Chọn danh sách</h5>
            {isCreatedSet ? (
              <>
              <div className="d-flex">

              <input
                  placeholder="Nhập tên danh sách"
                  className="form-control"
                  name="wordSetName"
                  required
                  onChange={(e) => {
                    handleChange(e);
                  }}
                  value={fileToVocab?.wordSetName}
                />

                <span className="btn btn-primary ms-2" onClick={() => setCreatedSet(!isCreatedSet)}>Chọn</span>
              </div>
               
              </>
            ) : (
              <>
               <div className="d-flex">
               <select
                  className="form-control "
                  id="inputGroupSelect01"
                  name="wordSetId"
                  onChange={(e) => handleChange(e)}
                  value={fileToVocab?.wordSetId}
                >
                  {wordSets.length === 0 && (
                    <option value={""}>
                    Chưa có danh sách nào
                  </option>
                  )}
                  {wordSets.map((item) => (
                    <option value={item.id}>
                      {item.name === "" ? "Chưa đặt tên" : item.name}
                    </option>
                  ))}
                </select>
                <span className="btn btn-primary ms-2" onClick={() => setCreatedSet(!isCreatedSet)}>Mới</span>
              
               </div>
              </>
            )}
          </div>
        </div>
        <div className="row mt-5 mb-4 mx-2">
          <div className="col">
            <h5>Mức độ</h5>
            <select
              className="form-control "
              id="inputGroupSelect01"
              name="level"
              onChange={(e) => handleChange(e)}
              value={fileToVocab?.level}
            >
              <option value={1}>Dễ</option>
              <option value={2}>Trung bình</option>
              <option value={3}>Khó</option>
            </select>
          </div>
          <div className="col">
            <h5>Ngôn ngữ</h5>
            <select
              className="form-control "
              id="inputGroupSelect01"
              name="language"
              onChange={(e) => handleChange(e)}
              value={fileToVocab?.language}
            >
              <option value={"english"}>Tiếng anh</option>
              <option value={"vietnamese"}>Tiếng việt</option>
              
              
            </select>
          </div>
          <div className="col">
            <h5>Thời gian</h5>

            <div className="input-group">
              <input
                type="number"
                className="form-control"
                id="validationServerUsername"
                placeholder="Thời gian"
                min={1}
                name="duration"
                value={fileToVocab?.duration}
                onChange={(e) => handleChange(e)}
              />
              <div className="input-group-prepend">
                <span className="input-group-text" id="inputGroupPrepend3">
                  Phút
                </span>
              </div>
            </div>
          </div>
        </div>
        <div className="text-end mx-3">
          <button className="btn btn-primary">Tạo bộ câu hỏi</button>
        </div>
      </form>
    </>
  );
};
export default FileToVocabularyBase;
