
import { Link } from 'react-router-dom'
import './Footer.css'

const Footer = () => {
  return (
<footer className="footer">
  <div className="container">
    <div className="row justify-content-center">
      <div className="col-md-12 text-center">
        <h2 className="footer-heading"><Link className="logo">QuizGuru</Link></h2>
        <p className="menu">
          <Link>Home</Link>
          <Link>Agent</Link>
          <Link>About</Link>
          <Link>Listing</Link>
          <Link>Blog</Link>
          <Link>Contact</Link>
        </p>
      
      </div>
    </div>
    <div className="row">
      <div className="col-md-12 text-center">
        <p className="copyright">
          Copyright © All rights reserved | This template is made with <i className="ion-ios-heart" aria-hidden="true" /> by <a href="https://colorlib.com" target="_blank">Colorlib.com</a>
        </p></div>
    </div>
  </div>
</footer>

  )
}

export default Footer
