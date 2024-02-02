import React, { useEffect, useState } from "react";
import Record from "../../components/Record/Record";
import "./index.css";
import UserService from "../../services/UserService";
import PreLoader from "../../components/PreLoader/PreLoader";
import CustomPagination from "../../components/Pagination/CustomPagination";
function RecordPage() {
  const [records, setRecords] = useState([]);
  const [isLoading, setLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPage, setTotalPage] = useState();
  const [totalProduct, setTotalProduct] = useState();
  const nextPage = () => {
    setCurrentPage(currentPage + 1);
  };

  const prevPage = () => {
    setCurrentPage(currentPage - 1);
  };
  const getAllRecords = async () => {
    setLoading(true);
    const response = await UserService.getCurrentUserRecords(currentPage);
    if (response?.status !== 400) {
      setRecords(response?.data);
      setTotalPage(response?.totalPages);
      setTotalProduct(response?.totalElements);
    }
    setLoading(false);
  };
  useEffect(() => {
    getAllRecords();
  }, [currentPage]);
  return (
    <>
      {isLoading ? (
        <PreLoader />
      ) : (
        <>
  
          <Record records={records} />{" "}
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
}

export default RecordPage;
