package com.khai.quizguru.filter;

import com.khai.quizguru.Exception.UnauthorizedException;
import com.khai.quizguru.security.JwtTokenProvider;
import com.khai.quizguru.security.SecurityUserService;
import com.khai.quizguru.security.UserPrincipal;
import com.khai.quizguru.utils.Constant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


@Slf4j
@NoArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private SecurityUserService securityUserService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, java.io.IOException {

        try {
            String token = tokenProvider.resolveToken(request);
            if(tokenProvider.validateToken(token) && StringUtils.hasText(token)) {

                String userId = tokenProvider.getUserIdFromJwt(token);
                UserPrincipal userPrincipal = securityUserService.loadUserById(userId);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userPrincipal,
                                null,
                                userPrincipal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }catch(Exception e) {
            throw new UnauthorizedException(Constant.UNAUTHORIZED_MSG);
        }

        filterChain.doFilter(request, response);
    }

}
