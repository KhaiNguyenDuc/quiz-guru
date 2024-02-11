import "./FileBase.css";
import { useState } from "react";
import QuizService from "../../../services/QuizService";
import { useNavigate } from "react-router-dom";
import {
  ALLOWED_TYPE,
  DOCX_TYPE,
  PDF_TYPE,
  TRY_AGAIN_MSG,
  TXT_TYPE,
  UNSUPPORT_MEDIA_TYPE_MSG,
} from "../../../utils/Constant";
import PreLoader from "../../PreLoader/PreLoader";
const FileBase = () => {
  const navigate = useNavigate();
  const [isError, setError] = useState("")
  const [isLoading, setLoading] = useState(false)
  const [filebase, setFileBase] = useState({
    type: 3,
    number: 5,
    language: "english",
    level: 1,
    duration: 10,
  });
  const [selectedFile, setFile] = useState();
  const handleChange = (e) => {
    setFileBase({ ...filebase, [e.target.name]: e.target.value });
  };
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true)
    let response = {};
    // Convert to second
    
    if (selectedFile.type === TXT_TYPE) {
      response = await QuizService.generateQuizByTxt({...filebase, duration: filebase.duration* 60}, selectedFile );
    } else if (selectedFile.type === PDF_TYPE) {
      response = await QuizService.generateQuizByPdf({...filebase, duration: filebase.duration* 60}, selectedFile);
    } else if (selectedFile.type === DOCX_TYPE) {
      response = await QuizService.generateQuizByDoc({...filebase, duration: filebase.duration* 60}, selectedFile);
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
            accept=".txt, .pdf, .docx, .doc"
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
              value={filebase?.type}
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
              value={filebase?.number}
            >
              <option value={5}>5 Câu hỏi</option>
              <option value={10}>10 Câu hỏi</option>
            </select>
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
              value={filebase?.level}
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
              value={filebase?.language}
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
                value={filebase?.duration}
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
export default FileBase;
