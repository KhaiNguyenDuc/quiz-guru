import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import "./IntroBox.css";
const IntroBox = ({ intro, button, edit }) => {
  return (
    <div>
      <div className="intro-box">
        <div className="intro-texts">
          <h1 className="intro-title">{intro.title}</h1>
          <p className="intro-description">{intro.description}</p>
          {button && <div className="button-container">{button()}</div>}
        </div>
        {edit && (
  <div className="intro-icon btn" onClick={() => edit()}>
  <FontAwesomeIcon icon="user-edit"/>
</div>
        )}
      
      </div>
    </div>
  );
};

export default IntroBox;
