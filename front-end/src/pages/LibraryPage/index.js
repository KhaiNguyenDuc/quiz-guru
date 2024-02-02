import React from 'react'
import IntroBox from '../../components/IntroBox/IntroBox'
import { INTRO_WORD_SET } from '../../Data/Data'
import WordSets from '../../components/WordSet/WordSets'

const LibraryPage = () => {
  return (
   <div>
     <IntroBox intro={INTRO_WORD_SET}/>
    <WordSets/> 
   </div>

  )
}

export default LibraryPage