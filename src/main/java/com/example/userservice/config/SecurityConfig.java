package com.example.userservice.config;

import com.example.userservice.filter.AuthenticationFilter;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 수업진행을 위한 WebSecurityConfigurerAdapter 상속
 * TODO 추후에 변경
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Environment environment;

    @Autowired
    public SecurityConfig(UserService userService, BCryptPasswordEncoder passwordEncoder, Environment environment) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
    }

    /**
     * 인증
     * loadUserByUsername 시 설정된 비밀번호와 비교하기 위해 passwordEncoder 설정
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    /**
     * 권한
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable();
        http
            .authorizeRequests()
                .antMatchers("/user-service/health-check").permitAll();
        http
            .authorizeRequests()
                .antMatchers("/user-service/users/**")
                    .hasIpAddress("localhost")
                    .and()
                    .addFilter(getAuthenticationFilter()); // 인증 Filter 등록
        http
            .headers()
                .frameOptions().disable();
    }

    /**
     * 인증 필터 등록
     */
    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter filter = new AuthenticationFilter(authenticationManager(), userService, environment);
        return filter;
    }
}
