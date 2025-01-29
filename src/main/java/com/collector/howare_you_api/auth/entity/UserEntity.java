package com.collector.howare_you_api.auth.entity;

public class UserEntity {

    private  String userId;
    private  String password;
    private  String email;
    private  String type;
    private  String role;

    //회원가입시 signrequestdto로 파라미터 받기
    public UserEntity(String userId, String password, String email, String typed, String role) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.type = "app";
        this.role = "ROLE_USER";
    }


    //회원가입시 signrequestdto로 파라미터 받기
    public UserEntity(String userId, String email, String type) {
        this.userId = userId;
        this.password = "dsf!@!";
        this.email = email;
        this.type = type;
        this.role = "ROLE_USER";
    }
}
