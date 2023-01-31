package com.ssafy.dietre.common.response;

import com.google.gson.annotations.SerializedName;

public class BaseRes {
    @SerializedName("message")
    private String message;
    @SerializedName("statusCode")
    private Integer statusCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}