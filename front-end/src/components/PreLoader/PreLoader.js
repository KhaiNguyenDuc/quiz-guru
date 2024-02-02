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
        {message && (<div className="text-dark">{message}</div>)}
    </div>
   
  )
}

export default PreLoader