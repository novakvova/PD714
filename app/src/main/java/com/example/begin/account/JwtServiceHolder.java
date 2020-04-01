package com.example.begin.account;

public interface JwtServiceHolder {
    void saveJWTToken(String token);
    String getToken();
    void removeToken();
}