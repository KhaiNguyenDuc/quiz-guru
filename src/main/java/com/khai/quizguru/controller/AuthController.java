package com.khai.quizguru.controller;

import com.khai.quizguru.model.RefreshToken;
import com.khai.quizguru.payload.request.LoginRequest;
import com.khai.quizguru.payload.request.RegisterRequest;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.payload.jwt.TokenRefreshRequest;
import com.khai.quizguru.payload.jwt.TokenRefreshResponse;
import com.khai.quizguru.payload.response.JsonResponse;
import com.khai.quizguru.payload.response.RegisterResponse;
import com.khai.quizguru.security.JwtTokenProvider;
import com.khai.quizguru.security.UserPrincipal;
import com.khai.quizguru.service.RefreshTokenService;
import com.khai.quizguru.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authManager;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<JsonResponse> register(@RequestBody RegisterRequest registerRequest){
        RegisterResponse userSaved = userService.createUser(registerRequest);
        return new ResponseEntity<>(new JsonResponse("success", userSaved), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JsonResponse> signIn(
            @RequestBody LoginRequest loginRequest){
        String accessToken = "";
        RefreshToken refreshToken;
        try{
            UsernamePasswordAuthenticationToken authReq
                    = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            Authentication auth = authManager.authenticate(authReq);
            accessToken = tokenProvider.generateToken(auth);
            SecurityContextHolder.getContext().setAuthentication(auth);
            UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();

            refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        }catch (Exception e){
            throw e;
        }
        TokenRefreshResponse tokenRefreshResponse = new TokenRefreshResponse(accessToken, refreshToken);
        return new ResponseEntity<>(new JsonResponse("success", tokenRefreshResponse),HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@RequestBody TokenRefreshRequest request) throws Exception {
        String requestRefreshToken = request.getRefreshToken();

        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken);
        refreshTokenService.verifyExpiration(refreshToken);
        User user = refreshToken.getUser();
        String accessToken = tokenProvider.generateTokenFromUserId(user.getId(), user.getRoles());

        return new ResponseEntity<>(new TokenRefreshResponse(accessToken, refreshToken),HttpStatus.OK);
    }
}
