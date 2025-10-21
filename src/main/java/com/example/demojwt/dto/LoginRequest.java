package com.example.demojwt.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private String captchaKey;     // 验证码唯一标识（比如前端生成的 key）
    private String captchaCode;    // 用户输入的验证码（比如 1234）
}
