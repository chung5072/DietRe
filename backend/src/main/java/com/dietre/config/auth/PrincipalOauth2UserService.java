package com.dietre.config.auth;

import com.dietre.common.auth.*;
import com.dietre.common.type.SocialLoginType;
import com.dietre.db.entity.User;
import com.dietre.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.dietre.common.type.SocialLoginType.*;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Map<String, Object> attributes = super.loadUser(userRequest).getAttributes();
        System.out.println("LOAD USER!!");
        System.out.println(attributes);

        OAuth2UserInfo oAuth2UserInfo = null;
        SocialLoginType type = null;

        switch (userRequest.getClientRegistration().getRegistrationId()) {
            case "kakao":
                oAuth2UserInfo = new KakaoUserInfo(attributes);
                type = KAKAO;
                break;
            case "google":
                oAuth2UserInfo = new GoogleUserInfo(attributes);
                type = GOOGLE;
                break;
            case "naver":
                oAuth2UserInfo = new NaverUserInfo(attributes);
                type = NAVER;
                break;
        }
        System.out.println(oAuth2UserInfo.getName());
        System.out.println(oAuth2UserInfo.getEmail());

        User user = userRepository.checkUser(oAuth2UserInfo.getEmail(), type);

        if (user == null) { // 회원 가입
            user = userRepository.save(User.builder()
                    .email(oAuth2UserInfo.getEmail())
                    .type(type)
                    .build());
        }


//        String token = jwtProvider.createToken("test", user.getId());
        return new PrincipalDetails(user, attributes);
    }
}
