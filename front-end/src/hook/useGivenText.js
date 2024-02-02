import { useContext } from 'react'
import { GivenTextContext } from '../context/GivenTextContext'

const useGivenText = () => {
  return useContext(GivenTextContext)
}

export default useGivenText
