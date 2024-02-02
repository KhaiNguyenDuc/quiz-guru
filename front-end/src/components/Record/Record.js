import { useNavigate } from "react-router-dom";
import "./Record.css"

function Record({records}) {
  const navigate = useNavigate();
 
  const handleReviewRecord = (e, recordId) => {
    navigate(`/member/record/${recordId}`)
  }
    return (
        <div className="quiz-history-container">
          <h2 className="my-3">Lịch sử làm bài</h2>
          <table className="quiz-history-table">
            <thead>
              <tr>
                <th className="text-center">Số thứ tự</th>
                <th className="text-center">Ngày làm</th>
                <th className="text-center">Số câu hỏi</th>
                <th className="text-center">Thời gian</th>
                <th className="text-center">Còn lại</th>
                <th className="text-center">Số câu trả lời đúng</th>
                <th className="text-center">Điểm</th>
                <th className="text-center">Chi tiết</th>
              </tr>
            </thead>
            <tbody>
              {records.map((record, index) => (
                <tr key={index}>
                  <td className="text-center">{index}</td>
                  <td className="text-center">{record.createDate}</td>
                  <td className="text-center">{record.recordItems.length}</td>  
                  <td className="text-center">{record.duration / 60} phút</td>
                  <td className="text-center">{(record.timeLeft / 60).toFixed(2)} phút</td>
                  <td className="text-center">{record.score}</td>
                  <td className="text-center">{((record.score * 100) / record.recordItems.length).toFixed(2)} % </td>
                  <td className="text-center">
                    <button className="btn btn-primary" onClick={(e) => handleReviewRecord(e, record.id)}>Xem</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
     
        </div>
      );
}

export default Record