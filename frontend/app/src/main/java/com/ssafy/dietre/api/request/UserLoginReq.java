package com.ssafy.dietre.api.request;

public class UserLoginReq {
    private String id;
    private String email;
    private String name;
    private String type;

    public UserLoginReq(String id, String email, String name, String type) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
