import React, { useState } from 'react'

function Lexemes({entry}) {
    const [showAllSenses, setShowAllSenses] = useState(false);
  return (
    <>
    {entry.lexemes.map((lexeme, index) => (
     <>
      <div key={index}>
      <hr className="bg-dark" />
       <div key={lexeme.lemma + lexeme.partOfSpeech} className="my-4">
         <h4>{lexeme.partOfSpeech}</h4>

         {lexeme.senses.slice(0, showAllSenses ? undefined : 2).map((sense, senseIndex) => (
         <div key={senseIndex}>
           <b>{sense.definition}</b>

           {sense.synonyms && sense.synonyms.length > 0 && (
             <p>Đồng nghĩa: {sense.synonyms.join(", ")}</p>
           )}
           <div>
             {sense.usageExamples && sense.usageExamples.length > 0 && (
               <>
                 <p> Ví dụ: </p>
                 <ul>
                   {sense.usageExamples.map((example, exampleIndex) => (
                     <li key={exampleIndex}>{example}</li>
                   ))}
                 </ul>
               </>
             )}
           </div>
         </div>
       ))}
        {lexeme.senses.length > 2 && (
         <button className="my-2 btn btn-secondary" onClick={(e) => {e.stopPropagation(); setShowAllSenses(!showAllSenses)}}>
           {showAllSenses ? "Ẩn" : "Hiển thị thêm"}
         </button>
       )}
       </div>
      </div>
     </>
   ))}
    </>
  )
}

export default Lexemes