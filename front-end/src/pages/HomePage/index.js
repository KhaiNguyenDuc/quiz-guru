import { useEffect } from "react";
import FileBase from "../../components/Setup/FileBase/FileBase";
import OptionTab from "../../components/Setup/OptionTab";
import TextBase from "../../components/Setup/TextBase/TextBase";
import "./index.css"
import { useParams } from "react-router-dom";
import VocabularyBase from "../../components/Setup/VocabularyBase/VocabularyBase";
import { BASIC_OPTIONS } from "../../Data/Data";
const HomePage = () => {
  let {option} = useParams()
  const getTab = () =>{
    return (
      option === null ? <TextBase/> : 
      option === "file"? <FileBase/> :
      option === "vocabulary"? <VocabularyBase/> : <TextBase/>
    )
  }
  return (
    <>
      <div className="app-container">
        <div className="setup-box">
          <OptionTab options={BASIC_OPTIONS}/>
          
          {getTab()}
        </div>
      </div>
    </>
  );
};
export default HomePage;
