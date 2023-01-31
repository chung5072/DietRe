package com.ssafy.dietre;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Dimension;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import com.ssafy.dietre.api.LoginApiCall;
import com.ssafy.dietre.api.request.UserLoginReq;
import com.ssafy.dietre.databinding.ActivityLoginBinding;
import com.ssafy.dietre.ui.survey.PhysicFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    // 로그인 페이지
    private ActivityLoginBinding binding_login;
    GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN = 123;
    private static final String TAG = "LoginActivity";

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fragment - xml 파일 연결
        binding_login = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding_login.getRoot());

        // 화면에서 사용하는 버튼
        // 구글 로그인 버튼
        SignInButton googleBtn = (SignInButton) binding_login.btnGoogle;
        // 카카오 로그인 버튼
        LinearLayout kakaoBtn = (LinearLayout) binding_login.btnKakao;
        // 네이버 로그인 버튼
        LinearLayout naverBtn = (LinearLayout) binding_login.btnNaver;

        // 버튼과 클릭 이벤트 연결
        // 카카오 로그인 버튼과 이벤트 연결
        // 구글 로그인 버튼과 이벤트 연결
        googleBtn.setOnClickListener(this);
        kakaoBtn.setOnClickListener(this);
        // 네이버 로그인 버튼과 이벤트 연결
        naverBtn.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() // email addresses도 요청함
                .build();

        // 위에서 만든 GoogleSignInOptions을 사용해 GoogleSignInClient 객체를 만듬
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

        // 기존에 로그인 했던 계정을 확인한다.
        GoogleSignInAccount gsa = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);

        // 구글 로그인 세팅
        TextView textGoogle = (TextView) binding_login.btnGoogle.getChildAt(0);
        textGoogle.setText(getString(R.string.login_google));
        textGoogle.setGravity(Gravity.CENTER);
        textGoogle.setTextSize(Dimension.SP, 20);
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);
            System.out.println(acct + "ACCT");
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                Log.d(TAG, "handleSignInResult:personName " + personName);
                Log.d(TAG, "handleSignInResult:personGivenName " + personGivenName);
                Log.d(TAG, "handleSignInResult:personEmail " + personEmail);
                Log.d(TAG, "handleSignInResult:personId " + personId);
                Log.d(TAG, "handleSignInResult:personFamilyName " + personFamilyName);
                Log.d(TAG, "handleSignInResult:personPhoto " + personPhoto);
                System.out.println("보낼 것  : " + personName + " " + personEmail + " " + personId + " " + "GOOGLE");
                LoginApiCall.call(new UserLoginReq(personId, personEmail, personName, "GOOGLE"), this);
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        System.out.println("CLICK=>...");
        System.out.println("ID : " + view.getId());
        switch (view.getId()) {
            case R.id.btn_naver: {
                OAuthLogin mOAuthLoginModule;
                Context mContext = getApplicationContext();

                mOAuthLoginModule = OAuthLogin.getInstance();
                mOAuthLoginModule.init(
                        mContext
                        , BuildConfig.NAVER_ID
                        , BuildConfig.NAVER_SECRET
                        , "DietRe"
                        //,OAUTH_CALLBACK_INTENT
                        // SDK 4.1.4 버전부터는 OAUTH_CALLBACK_INTENT변수를 사용하지 않습니다.
                );

                @SuppressLint("HandlerLeak")
                OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
                    @Override
                    public void run(boolean success) {
                        if (success) {
                            String accessToken = mOAuthLoginModule.getAccessToken(mContext);
                            String refreshToken = mOAuthLoginModule.getRefreshToken(mContext);
                            long expiresAt = mOAuthLoginModule.getExpiresAt(mContext);
                            String tokenType = mOAuthLoginModule.getTokenType(mContext);

                            Log.i("LoginData", "accessToken : " + accessToken);
                            Log.i("LoginData", "refreshToken : " + refreshToken);
                            Log.i("LoginData", "expiresAt : " + expiresAt);
                            Log.i("LoginData", "tokenType : " + tokenType);

                            new Thread(() -> {
                                String response = mOAuthLoginModule.requestApi(mContext, accessToken,
                                        "https://openapi.naver.com/v1/nid/me");
                                try {
                                    JSONObject loginResult = new JSONObject(response);
                                    if (loginResult.getString("resultcode").equals("00")) {
                                        JSONObject userProfile = loginResult.getJSONObject("response");
                                        String id = userProfile.getString("id");
                                        String email = userProfile.getString("email");
                                        String nickname = userProfile.getString("nickname");
                                        System.out.println(id + " " + email + " " + nickname);
                                        LoginApiCall.call(new UserLoginReq(id, email, nickname, "NAVER"), LoginActivity.this);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }).start();

                        } else {
                            String errorCode = mOAuthLoginModule
                                    .getLastErrorCode(mContext).getCode();
                            String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
                            Toast.makeText(mContext, "errorCode:" + errorCode
                                    + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                        }
                    }

                    ;
                };

                mOAuthLoginModule.startOauthLoginActivity(LoginActivity.this, mOAuthLoginHandler);
                break;
            }
            case R.id.btn_google: {
                // TODO 구글 로그인 버튼 클릭 이벤트
                loginGoogle();
                break;
            }
            case R.id.btn_kakao: {
                System.out.println("KAKAO CLICK");
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)) {
                    Log.e("login!", "login!");
                    kakaoLogin();
                } else {
                    Log.e("accountlogin!", "accountlogin!");
                    kakaoAccountLogin();
                }
                break;
            }
        }
    }

    private void kakaoAccountLogin() {
        UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, (oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());
                getUserInfo();
            }
            return null;
        });
    }

    private void kakaoLogin() {
        UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, (oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());
                getUserInfo();
            }
            return null;
        });
    }

    private void getUserInfo() {
        UserApiClient.getInstance().me((user, meError) -> {
            if (meError != null) {
                Log.e(TAG, "사용자 정보 요청 실패", meError);
            } else {
                System.out.println("로그인 완료");
                Log.i(TAG, user.toString());
                {
                    Log.i(TAG, "사용자 정보 요청 성공" +
                            "\n회원번호: " + user.getId() +
                            "\n이메일: " + user.getKakaoAccount().getEmail());
                }
                Account user1 = user.getKakaoAccount();
                System.out.println("사용자 계정" + user1);
                System.out.println("보낼것 : " + user1.getProfile().getNickname() + " " + user1.getEmail() + " " + user.getId() + " " + "KAKAO");
                try {
                    System.out.println("LOG THIS : " + LoginActivity.this);
                    System.out.println("THIS : " + this);
                    LoginApiCall.call(new UserLoginReq(Long.toString(user.getId()), user1.getEmail(), user1.getProfile().getNickname(), "KAKAO"), LoginActivity.this);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
    }

    private void loginGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);'
        System.out.println(requestCode + " REQUEST CODE ");
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
}
