import "./VocabularyBase.css";
import { useEffect, useState } from "react";
import QuizService from "../../../services/QuizService";
import { useNavigate } from "react-router-dom";
import PreLoader from "../../PreLoader/PreLoader";
import {
  GENERATE_LENGTH_INVALID,
  TRY_AGAIN_MSG,
} from "../../../utils/Constant";
import WordSetService from "../../../services/WordSetService";
import { validateInput } from "../../../utils/Utils";
import UserService from "../../../services/UserService";
const VocabularyBase = ({ defaultVocabulary, wordSetId }) => {
  const navigate = useNavigate();
  const [isLoading, setLoading] = useState(false);
  const [wordSets, setWordSets] = useState([]);
  const [isError, setError] = useState("");
  const [isCreatedSet, setCreatedSet] = useState(true);
  const [vocabularyBase, setVocabulary] = useState({
    names: defaultVocabulary ? defaultVocabulary.join(",") : "",
    type: 1,
    number: 5,
    language: "english",
    level: 1,
    duration: 10,
    wordSetId: "",
    wordSetName: "",
    isDoQuiz: true,
  });
  const handleChange = (e) => {
    setVocabulary({ ...vocabularyBase, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    // Validate input
    e.preventDefault();
    if (vocabularyBase?.names === "" || !validateInput(vocabularyBase?.names)) {
      setLoading(false);
      setError(GENERATE_LENGTH_INVALID);
      return;
    }

    setLoading(true);

    // Convert input string to input array of vocabulary
    const vocabulary = vocabularyBase?.names.split(/[,\n]+/);

    // Create quiz
    const quizResponse = await QuizService.generateQuizByVocabulary({
      ...vocabularyBase,
      names: vocabulary,
      duration: vocabularyBase.duration * 60,
      wordSetId: wordSetId
        ? wordSetId
        : !isCreatedSet && vocabularyBase.wordSetId === ""
        ? wordSets[0]?.id
        : vocabularyBase.wordSetId,
    });

    if (quizResponse?.status === 400 || quizResponse?.status === 500) {
      setLoading(false);
      setError(TRY_AGAIN_MSG);
    } else {
      if (vocabularyBase.isDoQuiz === false) {
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
        {defaultVocabulary === undefined && (
          <>
            <label htmlFor="text-content" className="form-label">
              <h5>Nhập nội dung cần tạo câu hỏi</h5>
            </label>
            <textarea
              placeholder="Nhập danh sách từ vựng cần ôn tập, mỗi từ cách nhau bởi dấu ',' hoặc xuống dòng. Ví dụ: sleep, walk, study,... Hoặc
          Sleep
          Walk
          Study
          ..."
              className="form-control"
              autoFocus
              id="text-content"
              rows={7}
              defaultValue={""}
              name="names"
              onChange={(e) => {
                handleChange(e);
                setError("");
              }}
              value={
                defaultVocabulary ? defaultVocabulary : vocabularyBase?.name
              }
              disabled={defaultVocabulary ? true : false}
            />
          </>
        )}

        <div className="row my-5 mx-2">
          <div className="col">
            <h5>Loại</h5>
            <select
              className="form-control "
              id="inputGroupSelect01"
              name="type"
              onChange={(e) => handleChange(e)}
              value={vocabularyBase?.type}
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
              value={vocabularyBase?.number}
            >
              <option value={5}>5 Câu hỏi</option>
              <option value={10}>10 Câu hỏi</option>
            </select>
          </div>
          {defaultVocabulary === undefined && (
            <>
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
                        value={vocabularyBase?.wordSetName}
                      />

                      <span
                        className="btn btn-primary ms-2"
                        onClick={() => setCreatedSet(!isCreatedSet)}
                      >
                        Chọn
                      </span>
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
                        value={vocabularyBase?.wordSetId}
                      >
                        {wordSets.length === 0 && (
                          <option value={""}>Chưa có danh sách nào</option>
                        )}
                        {wordSets.map((item) => (
                          <option value={item.id}>
                            {item.name === "" ? "Chưa đặt tên" : item.name}
                          </option>
                        ))}
                      </select>
                      <span
                        className="btn btn-primary ms-2"
                        onClick={() => setCreatedSet(!isCreatedSet)}
                      >
                        Mới
                      </span>
                    </div>
                  </>
                )}
              </div>
            </>
          )}
        </div>
        <div className="row mt-5 mb-3 mx-2">
          <div className="col">
            <h5>Mức độ</h5>
            <select
              className="form-control "
              id="inputGroupSelect01"
              name="level"
              onChange={(e) => handleChange(e)}
              value={vocabularyBase?.level}
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
              value={vocabularyBase?.language}
            >
                            <option value={"english"}>English</option>
              <option value={"vietnamese"}>Tiếng việt</option>
              <option value={"japanese"}>Japanese</option>
              <option value={"chinese"}>Chinese</option>
              <option value={"khmer"}>Campuchia</option>
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
                value={vocabularyBase?.duration}
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
        <div className="row mt-2 mx-2">
          <div className="col">
            <div className="form-check">
              <input
                className="form-check-input"
                type="checkbox"
                value={vocabularyBase.isDoQuiz}
                defaultChecked
                id="flexCheckDefault"
                onChange={() =>
                  setVocabulary({
                    ...vocabularyBase,
                    isDoQuiz: !vocabularyBase.isDoQuiz,
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
export default VocabularyBase;
