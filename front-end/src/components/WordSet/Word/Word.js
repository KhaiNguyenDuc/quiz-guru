import "./Word.css";
import Pronounciation from "./Pronounciation";
import Lexemes from "./Lexemes";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import { useState } from "react";
import WordService from "../../../services/WordService";
const Word = ({ setWords, word, isFlashCard, id, content }) => {

  const [htmlContent, setHtmlContent] = useState("");
  const [editModalIsOpen, setEditIsOpen] = useState(false);

  const handleSave = async () => {
    const response = WordService.updateDefinition(id, htmlContent);
    if (response?.status !== 400) {
      setEditIsOpen(false);
      setWords(prevWords => prevWords.map(prevWord => {
        if (prevWord.id === id) {
          return { ...prevWord, content: htmlContent };
        } else {
          return prevWord;
        }
      }));
    }
  };
  return (
    <div className="word card">
      <div className="card-body">
        {!isFlashCard && (
          <button
            onClick={() => setEditIsOpen(!editModalIsOpen)}
            className="btn btn-primary"
            style={{ marginLeft: "90%" }}
          >
            <FontAwesomeIcon icon="pencil" />
          </button>
        )}
        <Pronounciation entry={word.entries[0]} />
        {
          <>
            {editModalIsOpen ? (
              <>
                <div>
                  <ReactQuill
                    theme="snow"
                    style={{ backgroundColor: "white" }}
                    placeholder="Nhập nội dung liên quan đến từ vựng tại đây"
                    value={htmlContent}
                    onChange={(html) => {
                      setHtmlContent(html);
                    }}
                  />
                </div>

                <button
                  className="btn btn-success"
                  onClick={() => handleSave()}
                >
                  Lưu
                </button>
              </>
            ) : (
              <>
                {content !== null ? (
                  <>
                    <hr className="bg-dark" />
                    <div className="my-4">
                      <p dangerouslySetInnerHTML={{ __html: content }} />
                    </div>
                  </>
                ) : (
                  <>
                    {word.entries.map((entry) => {
                      if (JSON.stringify(entry) !== "{}") {
                        return <Lexemes entry={entry} />;
                      }
                      return <></>;
                    })}
                  </>
                )}
              </>
            )}
          </>
        }
      </div>
    </div>
  );
};

export default Word;
