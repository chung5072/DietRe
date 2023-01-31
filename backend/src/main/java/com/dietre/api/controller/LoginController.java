package com.dietre.api.controller;

import com.dietre.api.request.UserLoginReq;
import com.dietre.api.response.LoginRes;
import com.dietre.api.service.UserService;
import com.dietre.common.model.response.BaseRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<LoginRes> login(@RequestBody UserLoginReq request) {
        String jwt = userService.signUpAndLogin(request);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwt);
        Boolean hasUserInfo = userService.checkUserInfo(request);

        return ResponseEntity.status(200).headers(headers).body(LoginRes.of(200, "Success", hasUserInfo));
    }

}
