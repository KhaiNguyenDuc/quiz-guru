import { VOCABULARY_OPTIONS } from "../../Data/Data";
import FileToVocabularyBase from "../../components/Setup/FileToVocabularyBase/FIleToVocabularyBase";
import OptionTab from "../../components/Setup/OptionTab";
import TextToVocabularyBase from "../../components/Setup/TextToVocabularyBase/TextToVocabularyBase";
import VocabularyBase from "../../components/Setup/VocabularyBase/VocabularyBase";
import { useParams } from "react-router-dom";
const VocabularyPage = () => {  
  const {option} = useParams()
  const getTab = () =>{
    return  (
        option === "text"? <VocabularyBase/> :
        option === "text-to-vocab" ? <TextToVocabularyBase/> : 
        option === "file-to-vocab" ? <FileToVocabularyBase/> : <VocabularyBase/> 
    )
    
  }
  return (
    <>
      <div className="app-container">
        <div className="setup-box">
          <OptionTab options={VOCABULARY_OPTIONS}/>
          
          {getTab()}
        </div>
      </div>
    </>
  );
}

export default VocabularyPage