package com.dietre.api.service;

import com.dietre.api.request.UserInfoRegisterReq;
import com.dietre.api.request.UserInfoUpdateReq;
import com.dietre.common.exception.DuplicateRowException;
import com.dietre.api.request.UserLoginReq;
import com.dietre.common.auth.GoogleUserInfo;
import com.dietre.common.auth.JwtProvider;
import com.dietre.common.type.SocialLoginType;
import com.dietre.common.util.AgeCalculator;
import com.dietre.db.entity.User;
import com.dietre.db.entity.UserInfo;
import com.dietre.db.repository.UserInfoRepository;
import com.dietre.db.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final JwtProvider jwtProvider;

    public void registerUser(User user) {
        userRepository.save(user);
    }

    public void registerUserInfo(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).get();
        userRepository.delete(user);
    }

    public User buildUser(Long userId, UserInfoRegisterReq req) {
        if (userRepository.findById(userId).get().getUserInfo() != null) {
            throw new DuplicateRowException();
        }
        User user = userRepository.findById(userId).get();
        UserInfo userInfo = req.dtoToEntity(userId);
        user.setUserInfo(userInfo);

        return user;
    }

    public UserInfo rebuildUserInfo(Long userId, UserInfoUpdateReq req) {
        UserInfo userInfo = getUserInfo(userId);

        return req.updateEntity(userInfo);
    }

    public UserInfo getUserInfo(Long userId) {
        User user = userRepository.findById(userId).get();
        return user.getUserInfo();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).get();
    }

    public String signUpAndLogin(UserLoginReq request) {
        User user = userRepository.checkUser(request.getId(), request.getEmail(), SocialLoginType.valueOf(request.getType().name()));
        if (user == null) {
            user = userRepository.save(User.builder()
                    .socialLoginId(request.getId())
                    .nickname(request.getName())
                    .email(request.getEmail())
                    .type(SocialLoginType.valueOf(request.getType().name()))
                    .build());
        }
        return jwtProvider.createToken("jwt", user.getId());
    }

    public Boolean checkUserInfo(UserLoginReq request) {
        User user = userRepository.checkUser(request.getId(), request.getEmail(), request.getType());
        if (user.getUserInfo() == null) {
            return false;
        }
        return true;
    }
}
