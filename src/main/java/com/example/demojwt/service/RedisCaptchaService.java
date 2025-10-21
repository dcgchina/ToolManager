package com.example.demojwt.service;

import com.example.demojwt.model.CaptchaResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redis实现
 */
@Service
public class RedisCaptchaService {

    private final StringRedisTemplate redisTemplate;

    // 验证码过期时间（秒），比如 5 分钟
    @Value("${captcha.expire.seconds:300}")
    private int expireSeconds;

    public RedisCaptchaService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 生成一个图形验证码（4位数字），并存入 Redis
     * @return 验证码的唯一 key，前端登录时需要回传
     */
    public CaptchaResult generateImageCaptcha() {
        String captchaKey = UUID.randomUUID().toString();
        String code = generateRandomCode(4); // 4位数字，如 "1234"
        redisTemplate.opsForValue().set(captchaKey, code, expireSeconds, TimeUnit.SECONDS);
        return new CaptchaResult(captchaKey, code);
    }


    /**
     * 校验用户输入的图形验证码是否正确
     * @param captchaKey 之前生成的验证码 key
     * @param userInputCode 用户输入的验证码
     * @return 是否匹配
     */
    public boolean validateImageCaptcha(String captchaKey, String userInputCode) {
        String correctCode = redisTemplate.opsForValue().get(captchaKey);
        if (correctCode == null) {
            return false; // 验证码不存在或已过期
        }
        // 验证成功后删除验证码（一次性！）
        redisTemplate.delete(captchaKey);
        return correctCode.equals(userInputCode);
    }

    /**
     * 生成指定位数的随机数字验证码
     */
    private String generateRandomCode(int length) {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10)); // 0~9
        }
        return code.toString();
    }
}