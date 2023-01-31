package com.dietre.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // 설정 클래스에 붙이는 어노테이션, 스프링 프로젝트가 시작 될 때 스프링 시큐리티 설정 내용에 반영되도록 함
@EnableWebSecurity // 스프링 시큐리티를 활성화 하는 애노테이션
@RequiredArgsConstructor
public class SecurityConfig {

    private final TestJwtAuthFilter testJwtAuthFilter;
    private final TestJwtExceptionFilter testExceptionFilter;
    private final PrincipalOauth2UserService userService;
    private final OAuth2SuccessHandler successHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .cors().and() // CorsFilter 활성화
                .csrf().disable()  // csrf 사용 disable
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 스프링 시큐리티에서 세션 관리 안하겠다(jwt 방식이므로)
                .and()
                .authorizeRequests() // 이제 인증 정차에 대한 설정을 진행하겠다
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 해당 url은 인증되지 않아도 통과
                .antMatchers("/test/permit-all").permitAll()
                .anyRequest().permitAll(); // 요청 내에 스프링 시큐리티 컨텍스트 내에서 인증이 완료되어야 api 사용가능

//        http
//                .oauth2Login()
//                .successHandler(successHandler)
//                .userInfoEndpoint()
//                .userService(userService);

        http.addFilterBefore(testJwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(testExceptionFilter, TestJwtAuthFilter.class);

        return http.build();


    }
}

