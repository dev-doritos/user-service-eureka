package com.example.userservice.filter;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserService userService;
    private final Environment environment;

    @Autowired
    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, Environment environment) {
        super(authenticationManager);
        this.userService = userService;
        this.environment = environment;
    }

    /**
     * Login 시에 request 로 부터 email, password RequestLogin 객체 생성
     * AuthenticationManager 에게 UsernamePasswordAuthenticationToken 으로 전달
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            RequestLogin requestLogin = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestLogin.getEmail()
                            , requestLogin.getPassword()
                            , new ArrayList<>()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 로그인 성공 > Authentication 객체로부터 User 객체 > token(JWT) 생성 > header 에 token, userId 설정
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        // loadUserByUserName 에서 생성한 User 객체
        String email = ((User) authResult.getPrincipal()).getUsername();

        // UserDTO 조회
        UserDTO userDTO = userService.getUserByEmail(email);

        // Jwt 작성
        String token = Jwts.builder()
                        .setSubject(userDTO.getUserId())
                        .setExpiration(new Date(System.currentTimeMillis()
                                + Long.parseLong(environment.getProperty("token.expiration_time"))))
                        .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
                        .compact();

        System.err.println(token);

        // header 추가 : Controller 에서 진행하는 것 권장
        response.addHeader("token", token);
        response.addHeader("userId", userDTO.getUserId());
    }
}
