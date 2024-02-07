export const validateInput = (inputString) => {
    // Define the regular expression to match
    var expression = /^[a-zA-Z\s,]+$/;
  
    // Use the test() method to check if the inputString does not match the expression
    if (!expression.test(inputString)) {
      return false; // Input does not match the expression
    }
  
    return true; // Input matches the expression
  }

export const validateJwtToken = (token) => {
  if(token && token !== ""){
    return true
  }
  return false
}

export const isValidEmail = (email) => {
  // Regular expression for basic email validation
  var emailRegex = /\S+@\S+\.\S+/;
  return emailRegex.test(email);
}