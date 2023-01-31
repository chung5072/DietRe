package com.dietre.config.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class TestJwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        System.out.println("EXCEPTION FILTER");
        log.info("EXCEPTION FILTER");
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            System.out.println("HERE  JWT가 없습니다" + e.getMessage());
            response.setStatus(419);
        }

    }
}
