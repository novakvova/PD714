package com.example.begin.account;

public interface JwtServiceHolder {
    void SaveJWTToken(String token);
    String getToken();
}