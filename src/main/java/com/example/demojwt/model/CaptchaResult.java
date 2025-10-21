package com.example.demojwt.model;

public class CaptchaResult {
    private final String captchaKey;
    private final String captchaCode;

    public CaptchaResult(String captchaKey, String captchaCode) {
        this.captchaKey = captchaKey;
        this.captchaCode = captchaCode;
    }

    public String getCaptchaKey() {
        return captchaKey;
    }

    public String getCaptchaCode() {
        return captchaCode;
    }
}