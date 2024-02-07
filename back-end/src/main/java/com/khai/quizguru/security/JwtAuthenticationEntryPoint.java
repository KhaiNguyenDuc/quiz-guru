package com.khai.quizguru.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khai.quizguru.exception.ExceptionDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * Custom authentication entry point for handling unauthorized access.
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Handles unauthorized access by sending an HTTP 401 Unauthorized response with a JSON error message.
     *
     * @param httpServletRequest  The HTTP request being processed.
     * @param response            The HTTP response to be sent.
     * @param e                   The authentication exception that occurred.
     * @throws IOException        If an I/O error occurs while writing the response.
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        // Set the response status and content type
        int statusCode = HttpServletResponse.SC_UNAUTHORIZED;
        response.setStatus(statusCode);
        response.setContentType("application/json");

        // Create a map for the error message
        ExceptionDetails exceptionDetails = new ExceptionDetails(
                new Date(),
                e.getMessage(),
                ""
        );

        // Convert the error message to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonError = objectMapper.writeValueAsString(exceptionDetails);

        // Write the JSON error message to the response
        OutputStream out = response.getOutputStream();
        out.write(jsonError.getBytes());
        out.flush();
    }
}
