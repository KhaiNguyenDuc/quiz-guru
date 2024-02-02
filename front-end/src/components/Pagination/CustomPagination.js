import React from "react";
import Pagination from "react-bootstrap/Pagination";
import "./CustomPagination.css";

const CustomPagination = ({
  currentPage,
  totalPage,
  prevPage,
  nextPage,
  setCurrentPage,
  totalProduct,
}) => {
  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  return (
    <>
      {totalPage !== 0 ? (
        <>
          <Pagination>
            <Pagination.Prev onClick={prevPage} disabled={currentPage === 0} />
            {[...Array(totalPage).keys()].map((page) => (
              <Pagination.Item
                key={page + 1}
                active={page + 1 === currentPage + 1}
                onClick={() => handlePageChange(page)}
                className={`paginationItemStyle`}

              >
                
                {page + 1}
              </Pagination.Item>
            ))}
            <Pagination.Next
              onClick={nextPage}
              disabled={currentPage+1 === totalPage}
            />
          </Pagination>
        </>
      ) : (
        <div className="error-section">Không có dữ liệu</div>
      )}
    </>
  );
};

export default CustomPagination;
