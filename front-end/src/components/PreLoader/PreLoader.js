import "./PreLoader.css"
import ReactLoading from "react-loading";
function PreLoader({type, color, message}) {
  return (
    <div className="preloader-overlay">
      
       <ReactLoading
          type={type}
          color={color}
          height={100}
          width={100}
        />
        {message && (<div className="text-dark">{message}. Hãy đợi 2-3 phút (Vì api của chatgpt-3.5 rất chậm và xài vps free xD )</div>)}
    </div>
   
  )
}

export default PreLoader