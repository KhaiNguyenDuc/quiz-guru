package com.khai.quizguru.controller;


import com.khai.quizguru.exception.AccessDeniedException;
import com.khai.quizguru.exception.UnauthorizedException;
import com.khai.quizguru.model.user.RefreshToken;
import com.khai.quizguru.model.user.VerificationToken;
import com.khai.quizguru.payload.request.*;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.payload.jwt.TokenRefreshRequest;
import com.khai.quizguru.payload.jwt.TokenRefreshResponse;
import com.khai.quizguru.payload.response.JsonResponse;
import com.khai.quizguru.payload.response.RegisterResponse;
import com.khai.quizguru.security.JwtTokenProvider;
import com.khai.quizguru.security.UserPrincipal;
import com.khai.quizguru.service.EmailService;
import com.khai.quizguru.service.RefreshTokenService;
import com.khai.quizguru.service.UserService;
import com.khai.quizguru.service.VerifyTokenService;
import com.khai.quizguru.utils.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authManager;
    private final RefreshTokenService refreshTokenService;
    private final EmailService emailService;
    private final VerifyTokenService verifyTokenService;

    @PostMapping("/register")
    public ResponseEntity<JsonResponse> register(@RequestBody RegisterRequest registerRequest){
        RegisterResponse userSaved = userService.createUser(registerRequest);

        // Send verification email
        VerificationToken token = verifyTokenService.findTokenByUser(userSaved.getId());
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(userSaved.getEmail());
        emailRequest.setSubject(Constant.VERIFY_SUBJECT);
        emailRequest.setUserId(userSaved.getId());
        emailRequest.setBody(token.getToken());

        emailService.sendVerificationEmail(emailRequest);

        return new ResponseEntity<>(new JsonResponse("success", userSaved), HttpStatus.CREATED);
    }

    @PostMapping("/verify")
    public ResponseEntity<JsonResponse> verifyUser(
            @RequestBody VerifyRequest verifyRequest
    ){
        Boolean result = verifyTokenService.verifyUser(verifyRequest.getToken(), verifyRequest.getUsername());
        return new ResponseEntity<>(new JsonResponse("success", result), HttpStatus.CREATED);
    }

    @GetMapping("/send-verify")
    public ResponseEntity<JsonResponse> resendVerifyToken(
            @RequestParam String username

    ){

        Boolean result = userService.resendVerifyToken(username);
        return new ResponseEntity<>(new JsonResponse("success", result), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JsonResponse> signIn(
            @RequestBody LoginRequest loginRequest){
        String accessToken = "";
        RefreshToken refreshToken;
        UsernamePasswordAuthenticationToken authReq;
        if(Objects.nonNull(loginRequest.getEmail())){
            authReq  = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        }else{
            authReq = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        }
        Authentication auth = authManager.authenticate(authReq);
        accessToken = tokenProvider.generateToken(auth);
        SecurityContextHolder.getContext().setAuthentication(auth);
        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();

        refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        TokenRefreshResponse tokenRefreshResponse = new TokenRefreshResponse(accessToken, refreshToken);
        return new ResponseEntity<>(new JsonResponse("success", tokenRefreshResponse),HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@RequestBody TokenRefreshRequest request)  {
        String requestRefreshToken = request.getRefreshToken();

        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken);
        refreshTokenService.verifyExpiration(refreshToken);
        User user = refreshToken.getUser();
        String accessToken = tokenProvider.generateTokenFromUserId(user.getId(), user.getRoles());

        return new ResponseEntity<>(new TokenRefreshResponse(accessToken, refreshToken),HttpStatus.OK);
    }

    @PostMapping("/send-reset-password")
    public ResponseEntity<JsonResponse> sendResetPasssword(
            @RequestBody PasswordResetRequest request)  {

        String userId = userService.sendResetPassword(request);
        return new ResponseEntity<>(new JsonResponse("success", userId),HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<JsonResponse> resetPasssword(
            @RequestBody PasswordResetRequest request)  {

        userService.resetPassword(request);
        return new ResponseEntity<>(new JsonResponse("success", "success"),HttpStatus.OK);
    }




}
