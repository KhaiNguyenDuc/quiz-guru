package com.khai.quizguru.utils;

/**
 * Includes constant for multiple usage
 */
public class Constant {
    public static final String IMAGE_HOST = "http://localhost:8080/api/v1/images";
    public static final String UPLOAD_DIRECTORY = "/src/main/java";
    public static final String UPLOAD_USER_DIRECTORY = "/com/khai/quizguru/uploads/users";
    public static final String GET_USER_DIRECTORY = "src/main/java/com/khai/quizguru/uploads/users";

    public static final String RESOURCE_NOT_FOUND_MSG = "Not found the target resource";
    public static final String UNAUTHORIZED_MSG = "Unauthorized";
    public static final String ACCESS_DENIED_MSG = "You do not have permission to access this resource";
    public static final String RESOURCE_EXIST_MSG = "Resource already existed";
    public static final String INTERNAL_ERROR_EXCEPTION_MSG = "Internal error";
    public static final String INVALID_REQUEST_MSG = "Request invalid";
    public static final String INVALID_PASSWORD_MSG = "Password not secure enough ( length must >= 7 )";
    public static final String TOKEN_REFRESH_EXPIRED_MSG = "Refresh token was expired. Please make a new signin request";
    public static final String TOKEN_VERIFY_EXPIRED_MSG = "Verify token was expired. Please make a new signin request";

    public static final String TOKEN_INVALID_MSG = "Invalid refresh token";

    public static final String FILE_NOT_SUPPORT_MSG = "Not support this type of file.";
    public static final String VERIFY_SUBJECT = "Vui lòng xác thức tài khoản";
    public static final String PASSWORD_RESET_SUBJECT = "Thay đổi mật khẩu";

    public static final String PASSWORD_RESET_SUCCESS_SUBJECT = "Bạn đã thay đổi mật khẩu thành công";
}
