package com.dietre.config.auth;

import com.dietre.common.auth.JwtProvider;
import com.dietre.db.entity.User;
import com.dietre.db.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TestJwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, ExpiredJwtException {

        System.out.println(request.getServletPath());
        if (request.getServletPath().equals("/login")) {
            System.out.println("FILTERING");
            filterChain.doFilter(request, response);
        } else {
            System.out.println("TestJwtAuthFilter TestJwtAuthFilter");

            String jwt = request.getHeader("Authorization");
            System.out.println(request.getServletPath());

            if (jwt == null) {
                throw new RuntimeException();
            }
            try {
                System.out.println(jwtProvider.parseToken(jwt));
                System.out.println((((Map) jwtProvider.parseToken(jwt)).get("userId")).getClass());
                Long id = ((Number) ((Map) jwtProvider.parseToken(jwt)).get("userId")).longValue();
                System.out.println(id);
                User user = userRepository.findById(id).get();
                Authentication auth = new UsernamePasswordAuthenticationToken(new PrincipalDetails(user,
                        new HashMap<>()), null, null);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (UnsupportedJwtException e) {
                throw new RuntimeException(e);
            } catch (MalformedJwtException e) {
                throw new RuntimeException(e);
            } catch (ExpiredJwtException e) {
                throw new RuntimeException(e);
            } catch (SignatureException e) {
                throw new RuntimeException(e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            }


            filterChain.doFilter(request, response);
        }
    }

//    private Authentication getAuthentication(String id) {
//        UserDetails userDetails = userDetailsService.loadUserByUsername(id);
//        return new IdPwAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
//    }
}
