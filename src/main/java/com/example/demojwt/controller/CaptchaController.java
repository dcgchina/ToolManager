package com.example.demojwt.controller;

import com.example.demojwt.service.CaptChaService;
import com.example.demojwt.service.RedisCaptchaService;
import com.example.demojwt.util.CaptchaImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/capcha")
public class CaptchaController {
    @Autowired
    private CaptChaService captChaService;
    @Autowired
    private RedisCaptchaService redisCaptchaService;

    /**
     * GET /captcha
     * 返回一个图片（JPEG），图片中显示的是 Redis 中存储的验证码数字
     * 前端用  展示
     */
    @GetMapping("/generate")
    public ResponseEntity<Map<String, String>> getCaptchaImage() throws IOException {
        var result = redisCaptchaService.generateImageCaptcha(); // 返回 CaptchaResult
        String captchaKey = result.getCaptchaKey();
        String captchaCode = result.getCaptchaCode();

        var image = CaptchaImageUtil.generateCaptchaImage(captchaCode);
        byte[] imageBytes = CaptchaImageUtil.toJpegBytes(image);
        String base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes);
        String captchaImageSrc = "data:image/jpeg;base64," + base64Image;

        Map<String, String> response = new HashMap<>();
        response.put("captchaKey", captchaKey);
        response.put("captchaImage", captchaImageSrc);

        return ResponseEntity.ok(response);
    }
}
