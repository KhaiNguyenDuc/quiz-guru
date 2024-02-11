import { Link } from "react-router-dom"
import "./ErrorPage.css"

const ErrorPage =({status, title, message}) => {
    return (
        <div id="notfound">
		<div className="notfound">
			<div className="notfound-404">
				<h1>Oops!</h1>
			</div>
			<h2  className=" my-3">{status} - {title}</h2>
			<p>{message}.</p>
			<Link to={"/"}>Trở về trang chủ</Link>
		</div>
	</div>

    )
}

export default ErrorPage