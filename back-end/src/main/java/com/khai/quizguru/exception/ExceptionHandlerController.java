package com.khai.quizguru.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The ExceptionHandlerController class handles exceptions globally across the application.
 * It provides methods to handle different types of exceptions and return appropriate responses.
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    /**
     * Handles the ResourceNotFoundException and returns a ResponseEntity with a NOT_FOUND status code.
     * @param e The ResourceNotFoundException
     * @param request The WebRequest
     * @return ResponseEntity with ExceptionDetails and HttpStatus.NOT_FOUND
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDetails> resourceNotFound(
            ResourceNotFoundException e, WebRequest request){
        ExceptionDetails details = new ExceptionDetails(
                new Date(),
                e.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles the UnauthorizedException and returns a ResponseEntity with an UNAUTHORIZED status code.
     * @param e The UnauthorizedException
     * @param request The WebRequest
     * @return ResponseEntity with ExceptionDetails and HttpStatus.UNAUTHORIZED
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionDetails> unauthorized(
            UnauthorizedException e, WebRequest request
    ){
        ExceptionDetails details = new ExceptionDetails(
                new Date(),
                e.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(details, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles the InvalidRequestException and returns a ResponseEntity with a BAD_REQUEST status code.
     * @param e The InvalidRequestException
     * @param request The WebRequest
     * @return ResponseEntity with ExceptionDetails and HttpStatus.BAD_REQUEST
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ExceptionDetails> invalidRequest(
            InvalidRequestException e, WebRequest request
    ){
        ExceptionDetails details = new ExceptionDetails(
                new Date(),
                e.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles the InternalErrorException and returns a ResponseEntity with an INTERNAL_SERVER_ERROR status code.
     * @param e The InternalErrorException
     * @param request The WebRequest
     * @return ResponseEntity with ExceptionDetails and HttpStatus.INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<ExceptionDetails> internalError(
            InternalErrorException e, WebRequest request
    ){
        ExceptionDetails details = new ExceptionDetails(
                new Date(),
                e.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(details, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles the TokenRefreshException and returns a ResponseEntity with an INTERNAL_SERVER_ERROR status code.
     * @param e The TokenRefreshException
     * @param request The WebRequest
     * @return ResponseEntity with ExceptionDetails and HttpStatus.INTERNAL_SERVER_ERROR
     */
    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<ExceptionDetails> tokenRefresh(
            TokenRefreshException e, WebRequest request
    ){
        ExceptionDetails details = new ExceptionDetails(
                new Date(),
                e.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(details, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles the ResourceExistException and returns a ResponseEntity with a BAD_REQUEST status code.
     * @param e The ResourceExistException
     * @param request The WebRequest
     * @return ResponseEntity with ExceptionDetails and HttpStatus.BAD_REQUEST
     */
    @ExceptionHandler(ResourceExistException.class)
    public ResponseEntity<ExceptionDetails> resourceExist(
            ResourceExistException e, WebRequest request
    ){
        ExceptionDetails details = new ExceptionDetails(
                new Date(),
                e.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles the AccessDeniedException and returns a ResponseEntity with a FORBIDDEN status code.
     * @param e The AccessDeniedException
     * @param request The WebRequest
     * @return ResponseEntity with ExceptionDetails and HttpStatus.FORBIDDEN
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDetails> accessDenied(
            AccessDeniedException e, WebRequest request
    ){
        ExceptionDetails details = new ExceptionDetails(
                new Date(),
                e.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(details, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles the MethodArgumentNotValidException and returns a ResponseEntity with a BAD_REQUEST status code.
     * @param ex: Exception class
     * @param headers: Headers
     * @param status: Status
     * @param request: Request
     * @return ResponseEntity
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ExceptionDetails details = new ExceptionDetails(
                new Date(),
                errors,
                request.getDescription(false)
        );
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

}
