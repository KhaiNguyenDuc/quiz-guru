import "./TextBase.css";
import { useState } from "react";
import QuizService from "../../../services/QuizService";
import { useNavigate } from "react-router-dom";
import PreLoader from "../../PreLoader/PreLoader";
import { GENERATE_LENGTH_SHORT } from "../../../utils/Constant";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";

const TextBase = () => {
  let reactQuillRef = null;
  const navigate = useNavigate();
  const [isLoading, setLoading] = useState(false);
  const [isError, setError] = useState("");
  const [htmlContent, setHtmlContent] = useState("");
  const [textbase, setTextBase] = useState({
    content: "",
    type: 1,
    number: 5,
    language: "english",
    level: 1,
    duration: 10,
  });

  const handleContent = () => {
    const editor = reactQuillRef.getEditor();
    const unprivilegedEditor = reactQuillRef.makeUnprivilegedEditor(editor);
    const inpText = unprivilegedEditor.getText();
    setTextBase({...textbase, content: inpText})

  }
  const handleChange = (e) => {
    setTextBase({ ...textbase, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (textbase?.content === "" || textbase?.content.length <= 90) {
      setLoading(false);
      setError(GENERATE_LENGTH_SHORT);
      return;
    }
    setLoading(true);
    const response = await QuizService.generateQuizByText({
      ...textbase,
      htmlContent: htmlContent,
      duration: textbase.duration * 60,
    });
    if (response?.status !== 400) {
      setLoading(false);
      navigate(`/quiz/${response}`);
    }
  };

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

        <div>
          <label htmlFor="text-content" className="form-label">
            <h5>Nhập nội dung cần tạo câu hỏi</h5>
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
        </div>
        <div className="row mt-5 mx-2">
          <div className="col">
            <h5>Loại</h5>
            <select
              className="form-control "
              id="inputGroupSelect01"
              name="type"
              onChange={(e) => handleChange(e)}
              value={textbase?.type}
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
              value={textbase?.number}
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
              value={textbase?.level}
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
              value={textbase?.language}
            >
                            <option value={"english"}>English</option>
              <option value={"vietnamese"}>Tiếng việt</option>
              <option value={"japanese"}>Japanese</option>
              <option value={"chinese"}>Chinese</option>
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
                value={textbase?.duration}
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
export default TextBase;
