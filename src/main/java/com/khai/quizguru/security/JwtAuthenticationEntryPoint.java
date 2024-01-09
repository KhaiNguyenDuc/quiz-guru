package com.khai.quizguru.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khai.quizguru.Exception.ExceptionDetails;
import com.khai.quizguru.Exception.ResourceNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException e)
            throws java.io.IOException {
        // Set the response status and content type
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        // Create a map for the error message

        ExceptionDetails exceptionDetails = new ExceptionDetails(
                new Date(),
                "Sorry, You're not authorized to access this resource.",
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