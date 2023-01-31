package com.ssafy.dietre.common.provider;

import com.ssafy.dietre.api.ApiCallService;

public class ApiCallServiceProvider {
    private static class ApiCallServiceProviderHolder {
        private static ApiCallService instance = RetrofitProvider
                .provide()
                .create(ApiCallService.class);
    }

    public static ApiCallService provide() {
        return ApiCallServiceProviderHolder.instance;
    }

    public static ApiCallService rebuild() {
        ApiCallServiceProviderHolder.instance = RetrofitProvider
                .provide()
                .create(ApiCallService.class);

        return ApiCallServiceProviderHolder.instance;
    }
}
