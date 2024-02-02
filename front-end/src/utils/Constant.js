export const HOST = 'api/v1'
export const ROOT = 'http://localhost:8080'
export const GOOGLE_CLIENT_ID  = '990887837910-pml8dp7e0lhotrolcprggedblnfki2no.apps.googleusercontent.com'
export const TXT_TYPE = 'text/plain';
export const PDF_TYPE = 'application/pdf';
export const DOCX_TYPE = 'application/vnd.openxmlformats-officedocument.wordprocessingml.document';
export const ALLOWED_TYPE = [
    TXT_TYPE,
    PDF_TYPE,
    DOCX_TYPE
  ];

export const BASE_URL = ROOT + "/" + HOST
export const AUTH_URL = BASE_URL + "/auth"
export const USER_URL = BASE_URL + "/users"
export const QUIZ_URL = BASE_URL + "/quiz"
export const RECORD_URL = BASE_URL + "/records"
export const WORDSET_URL = BASE_URL + "/word-set"
export const WORD_URL = BASE_URL + "/words"
export const DICTIONARY_API = "https://api.dictionaryapi.dev/api/v2/entries/en/"
export const MULTIPLE_CHOICE_QUESTION = "MULTIPLE_CHOICE_QUESTION"
export const SINGLE_CHOICE_QUESTION = "SINGLE_CHOICE_QUESTION"
export const PASSWORD_MISSMATCH_MSG = "Mật khẩu không khớp."
export const INVALID_LOGIN_MSG = "Tên đăng nhập hoặc mật khẩu không đúng."
export const EXIST_USERNAME_MSG = "Tên đăng nhập đã tồn tại."
export const BLANK_LOGIN_MSG = "Tên đăng nhập hoặc mật khẩu không được trống."
export const UNSUPPORT_MEDIA_TYPE_MSG = "Chỉ hỗ trợ file .txt, .pdf, .docx."
export const TRY_AGAIN_MSG = "Có lỗi xảy ra, vui lòng thử lại sau."
export const GENERATE_LENGTH_INVALID = "Nội dung không hợp lệ."
export const GENERATE_LENGTH_SHORT = "Nội dung không được để trống hoặc ít hơn 90 ký tự."
export const EMPTY_NAME = "Chưa đặt tên"
export const USERNAME_EXIST = "Tên đăng nhập đã tồn tại"