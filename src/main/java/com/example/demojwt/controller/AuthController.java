package com.example.demojwt.controller;

import com.example.demojwt.dto.LoginRequest;
import com.example.demojwt.dto.LoginResponse;
import com.example.demojwt.model.User;
import com.example.demojwt.service.CaptChaService;
import com.example.demojwt.service.JwtService;
import com.example.demojwt.service.LoginService;
import com.example.demojwt.service.RedisCaptchaService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final LoginService loginService;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final CaptChaService captChaService;
    private final RedisCaptchaService redisCaptchaService;

    public AuthController(LoginService loginService, JwtService jwtService,CaptChaService captChaService,RedisCaptchaService redisCaptchaService) {
        this.loginService = loginService;
        this.jwtService = jwtService;
        this.captChaService = captChaService;
        this.redisCaptchaService = redisCaptchaService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest){
        // 1. 先校验验证码
        String captchaKey = loginRequest.getCaptchaKey();
        String captchaCode = loginRequest.getCaptchaCode();

        if (captchaKey == null || captchaCode == null ||
                !redisCaptchaService.validateImageCaptcha(captchaKey, captchaCode)) {
            return new LoginResponse(null, "验证码错误或已过期");
        }

        // 2. 再校验用户名和密码
        User byUserName = loginService.findByUserName(loginRequest.getUsername());
        if(byUserName == null || !passwordEncoder.matches(loginRequest.getPassword(), byUserName.getPassword())){
            System.out.println("登录失败！账户或密码错误！");
            return new LoginResponse(null,"登录失败！");
        }
        System.out.println("登录成功！");
        String token = jwtService.generateToken(loginRequest.getUsername());
        System.out.println("byUserName的值为：" + byUserName.getUsername() + ", 请求名字：" + loginRequest.getUsername());
        return new LoginResponse(token,"登陆成功！");
    }
}
