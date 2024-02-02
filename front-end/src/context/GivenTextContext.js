import React, { createContext, useState } from "react";

const GivenTextContext = createContext();

const GivenTextProvider = ({ children }) => {
  const [showText, setShowText] = useState(true);

  return (
    <GivenTextContext.Provider value={{ showText, setShowText }}>
      {children}
    </GivenTextContext.Provider>
  );
}


export { GivenTextProvider, GivenTextContext };