import React, { useEffect } from "react";
import { useParams } from "react-router-dom";
import WordSetService from "../../services/WordSetService";
import { useState } from "react";
import Word from "../../components/WordSet/Word/Word";
import IntroBox from "../../components/IntroBox/IntroBox";
import CustomPagination from "../../components/Pagination/CustomPagination";
import PreLoader from "../../components/PreLoader/PreLoader";
import WordService from "../../services/WordService";
import { useNavigate } from "react-router-dom";
import CustomModal from "../../components/CustomModal/CustomModal";
import VocabularyBase from "../../components/Setup/VocabularyBase/VocabularyBase";
import EditWordSetForm from "../../components/WordSet/EditWordSetForm";
const WordPage = () => {
  const navigate = useNavigate();
  const { wordSetId } = useParams();
  const [quizId, setQuizId] = useState();
  const [name, setName] = useState();
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPage, setTotalPage] = useState();
  const [totalProduct, setTotalProduct] = useState();
  const [isLoading, setLoading] = useState(false);
  const [modalIsOpen, setIsOpen] = useState(false);
  const [editModalIsOpen, setEditIsOpen] = useState(false);
  const [words, setWords] = useState([]);
  const [rawWords, setRawWords] = useState([]);
  const nextPage = () => {
    setCurrentPage(currentPage + 1);
  };

  const prevPage = () => {
    setCurrentPage(currentPage - 1);
  };

  const findWordsByWordSet = async (page) => {
    setLoading(true);
    const response = await WordSetService.findWordsByWordSet(wordSetId, page);
    if (response?.status !== 400) {
      const listWords = response?.data[0]?.words.map(
        (response) => response?.name
      );
      setTotalPage(response?.totalPages);
      setTotalProduct(response?.totalElements);
      setRawWords(listWords);
      setQuizId(response?.data[0]?.quizId);
      setName(response?.data[0]?.name);
      const wordResponse = await WordService.getDefinition(listWords, wordSetId);
      if (wordResponse?.status !== 400) {
        setWords(wordResponse.map((wordResponse) => {
          let parsedDefinition = wordResponse.definition; // Default to the original definition
      
          // Attempt to parse the definition only if it's a non-empty string
          if (wordResponse.definition && typeof wordResponse.definition === 'string') {
            try {
              parsedDefinition = JSON.parse(wordResponse.definition);
            } catch (error) {
              // Parsing failed, keep the original definition
            }
          }
      
          return {
            ...wordResponse, // Keep the original fields
            definition: parsedDefinition // Replace the "definition" field with parsed value or the original value if parsing failed
          };
        }));
      }
    }
    setLoading(false);
  };
  const handleQuiz = () => {
    if (quizId !== null) {
      navigate(`/quiz/${quizId}`);
    } else {
      setIsOpen(true);
    }
  };
  const button = () => {
    return (
      <button className="btn btn-success" onClick={() => handleQuiz()}>
        Luyện tập
      </button>
    );
  };

  useEffect(() => {
    findWordsByWordSet(currentPage);
  }, [wordSetId, currentPage]);

  const updateWordSet = () => {
    setEditIsOpen(true);
  };
  return (
    <>
      {isLoading ? (
        <PreLoader />
      ) : (
        <div>
          <IntroBox
            intro={{
              title: name,
              description: "Các từ trong danh sách",
            }}
            button={button}
            edit={() => updateWordSet()}
          />

          {words.map((word, index) => {

            if (word?.definition?.entries[0] !== undefined || word?.content !== null ) {
              return <Word setWords={setWords} word={word?.definition} id={word?.id} content={word?.content} key={index} />;
            }
            return <></>
          
          })}
          <div className="custom-pagination">
            <CustomPagination
              currentPage={currentPage}
              totalPage={totalPage}
              prevPage={prevPage}
              nextPage={nextPage}
              setCurrentPage={setCurrentPage}
              totalProduct={totalProduct}
            />
          </div>
          <CustomModal
            modalIsOpen={modalIsOpen}
            setIsOpen={setIsOpen}
            width={"600px"}
          >
            <VocabularyBase
              defaultVocabulary={rawWords}
              wordSetId={wordSetId}
            />
          </CustomModal>

          <CustomModal
            modalIsOpen={editModalIsOpen}
            setIsOpen={setEditIsOpen}
            width={"400px"}
          >
            <EditWordSetForm
              defaultName={name}
              setName={setName}
              setIsEditOpen={setEditIsOpen}
              wordSetId={wordSetId}
            />
          </CustomModal>
        </div>
      )}
    </>
  );
};

export default WordPage;
