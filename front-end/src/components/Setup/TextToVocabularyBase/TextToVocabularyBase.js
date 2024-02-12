import { useEffect, useState } from "react";
import QuizService from "../../../services/QuizService";
import { useNavigate } from "react-router-dom";
import PreLoader from "../../PreLoader/PreLoader";
import {
  GENERATE_LENGTH_INVALID,
  TRY_AGAIN_MSG,
} from "../../../utils/Constant";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import UserService from "../../../services/UserService";
const TextToVocabularyBase = () => {
  let reactQuillRef = null;
  const navigate = useNavigate();
  const [isLoading, setLoading] = useState(false);
  const [wordSets, setWordSets] = useState([]);
  const [isError, setError] = useState("");
  const [isCreatedSet, setCreatedSet] = useState(true);
  const [htmlContent, setHtmlContent] = useState("");
  const [textToVocab, setVocabulary] = useState({
    content: "",
    type: 1,
    number: 5,
    language: "english",
    level: 1,
    duration: 10,
    isDoQuiz: true,
    numberOfWords: 5,
    wordSetId: "",
    wordSetName: "",
  });
  const handleChange = (e) => {
    setVocabulary({ ...textToVocab, [e.target.name]: e.target.value });
  };
  const handleContent = () => {
    const editor = reactQuillRef.getEditor();
    const unprivilegedEditor = reactQuillRef.makeUnprivilegedEditor(editor);
    const inpText = unprivilegedEditor.getText();
    setVocabulary({...textToVocab, content: inpText})

  }

  const handleSubmit = async (e) => {
    // Validate input
    e.preventDefault();
    if (textToVocab?.content === "") {
      setLoading(false);
      setError(GENERATE_LENGTH_INVALID);
      return;
    }

    setLoading(true);

    // Create quiz
    const quizResponse = await QuizService.generateQuizByTextToVocab({
      ...textToVocab,
      content: textToVocab.content,
      duration: textToVocab.duration * 60,
      isDoQuiz: textToVocab.isDoQuiz,
      wordSetId: (!isCreatedSet && textToVocab.wordSetId === "") ? wordSets[0]?.id : textToVocab.wordSetId,
      htmlContent: htmlContent  
    });

    if (quizResponse?.status === 400 || quizResponse?.status === 500) {
      setLoading(false);
      setError(TRY_AGAIN_MSG);
    } else {
      setLoading(false);
      if (textToVocab.isDoQuiz === false) {
        navigate(`/member/word-set/${quizResponse}`);
      } else {
        navigate(`/quiz/${quizResponse}`);
      }
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


  return (
    <>
      <form className="mt-5" onSubmit={(e) => handleSubmit(e)}>
        {isLoading && (
          <PreLoader
            color={"black"}
            type={"bars"}
            message={"Đang khởi tạo bộ câu hỏi"}
          />
        )}
        {isError && <div className="error-section mb-3">{isError}</div>}

        <>
          <label htmlFor="text-content" className="form-label">
            <h5>Nhập văn bảng cần trích xuất từ vựng</h5>
          </label>
          <div >
            <ReactQuill
              theme="snow"
              style={{backgroundColor: 'white'}}
              placeholder="Nhập hoặc dán nội dung cần tạo câu hỏi tại đây, ít nhât 90 ký tự."
              value={htmlContent}
              onChange={(html) => {handleContent(); setHtmlContent(html);}}
              ref={(el) => { reactQuillRef = el }}
            />
          </div>
        </>

        <div className="row my-5 mx-2">
          <div className="col">
            <h5>Loại</h5>
            <select
              className="form-control "
              id="inputGroupSelect01"
              name="type"
              onChange={(e) => handleChange(e)}
              value={textToVocab?.type}
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
              value={textToVocab?.number}
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
              value={textToVocab?.numberOfWords}
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
                  value={textToVocab?.wordSetName}
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
                  value={textToVocab?.wordSetId}
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
              value={textToVocab?.level}
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
              value={textToVocab?.language}
            >
              <option value={"english"}>English</option>
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
                value={textToVocab?.duration}
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

        <div className="row mt-2 mb-4 mx-2">
          <div className="col">
            <div className="form-check">
              <input
                className="form-check-input"
                type="checkbox"
                value={textToVocab.isDoQuiz}
                defaultChecked
                id="flexCheckDefault"
                onChange={() =>
                  setVocabulary({
                    ...textToVocab,
                    isDoQuiz: !textToVocab.isDoQuiz,
                  })
                }
              />
              <label className="form-check-label" htmlFor="flexCheckDefault">
                Làm bài test
              </label>
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

export default TextToVocabularyBase;
