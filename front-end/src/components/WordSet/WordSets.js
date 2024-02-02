import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import WordSetCard from "./WordSetCard";
import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import UserService from "../../services/UserService";
import CustomPagination from "../Pagination/CustomPagination";
import PreLoader from "../PreLoader/PreLoader";
import WordSetService from "../../services/WordSetService";
import CustomModal from "../CustomModal/CustomModal";
import WordSetForm from "./WordSetForm";
const WordSets = () => {
  const [wordSets, setWordSets] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPage, setTotalPage] = useState();
  const [totalProduct, setTotalProduct] = useState();
  const [isLoading, setLoading] = useState(false);
  const nextPage = () => {
    setCurrentPage(currentPage + 1);
  };

  const prevPage = () => {
    setCurrentPage(currentPage - 1);
  };

  const getAllWordSets = async () => {
    setLoading(true);
    const response = await UserService.getCurrentUserWordSets(currentPage);
    if (response?.status !== 400) {
      setWordSets(response?.data);
      setTotalPage(response?.totalPages);
      setTotalProduct(response?.totalElements);
    }
    setLoading(false);
  };
  useEffect(() => {
    getAllWordSets();
  }, []);

  const handleDelete = async (wordSetId) => {
    const response = await WordSetService.deleteById(wordSetId);
    if(response?.status !== 400){
     setWordSets(wordSets.filter(wordSets => wordSets?.id !== wordSetId));
    }
  }
  const [modalIsOpen, setIsOpen] = useState(false);


  return (
    <>
      {isLoading ? (
        <PreLoader />
      ) : (
        <>
          <div className="row ">

            <div className="col-3 mt-3">
              <div className="word-set card">
                <div className="card-body d-flex align-items-center justify-content-center text-center"
                onClick={() => setIsOpen(true)}>
                  <div >
                    <FontAwesomeIcon icon={"plus"} size="2x" color="#213261" />
                    <h5
                      className="card-title my-2"
                      style={{ color: "#213261" }}
                      
                    >
                     Tạo danh sách từ
                    </h5>
                   
                  </div>
                </div>
              </div>
            </div>
            <CustomModal modalIsOpen={modalIsOpen} setIsOpen={setIsOpen}>
              <WordSetForm/>
            </CustomModal>


            {wordSets.map((wordSet, index) => (
              <div key={index} className={`col-3 mt-3`}>
                <WordSetCard wordSet={wordSet} handleDelete={() => handleDelete(wordSet?.id)} />
              </div>
            ))}
          </div>

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
        </>
      )}
    </>
  );
};

export default WordSets;
