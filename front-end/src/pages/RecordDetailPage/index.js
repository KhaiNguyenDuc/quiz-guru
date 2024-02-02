import { useEffect, useState } from "react";
import "./index.css";
import { useLocation } from "react-router-dom";
import useGivenText from "../../hook/useGivenText";
import RecordDetail from "../../components/Record/RecordDetail";
import UserService from "../../services/UserService";
import { useParams } from "react-router-dom";
import PreLoader from "../../components/PreLoader/PreLoader";
const RecordDetailPage = () => {
  let { id } = useParams();
  const [isLoading, setLoading] = useState(false);
  let { showText } = useGivenText();
  const [record, setRecord] = useState({});
  const getCurrentUserRecordById = async (recordId) => {
    setLoading(true);
    const response = await UserService.getCurrentUserRecordById(recordId);
    if (response?.status !== 400) {
      setRecord(response);
    }
    setLoading(false);
  };
  useEffect(() => {
    getCurrentUserRecordById(id);
  }, []);
  return (
    <>
      <div className="row result-page">
        

        {isLoading ? (
          <PreLoader color={"black"} type={"spin"} />
        ) : (
          <div
            className={` result-section ${showText ? "col" : " full-width"}`}
          >
            <RecordDetail record={record} />
          </div>
        )}
      </div>
    </>
  );
};

export default RecordDetailPage;
