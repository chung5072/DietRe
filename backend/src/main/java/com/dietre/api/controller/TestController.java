package com.dietre.api.controller;

import com.dietre.common.auth.JwtProvider;
import com.dietre.common.model.response.BaseRes;
import com.dietre.config.auth.PrincipalDetails;
import com.dietre.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/test")
@RestController
public class TestController {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @GetMapping("")
    public String test(@AuthenticationPrincipal PrincipalDetails authentication) {
        System.out.println("=====================TEST=============================");
        System.out.println(((PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId());
        System.out.println(authentication.getUser().getId());
        return "test";
    }

    @GetMapping("/test")
    public ResponseEntity test2(@AuthenticationPrincipal PrincipalDetails authentication) {
        return ResponseEntity.status(200).body(BaseRes.of(200, "test2"));
    }
}