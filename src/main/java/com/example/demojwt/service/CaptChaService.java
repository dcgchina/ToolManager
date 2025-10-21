package com.example.demojwt.service;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * 基于内存实现
 */
@Service
@Data
public class CaptChaService {
    // 模拟Redis存储
    private Map<String,String> captchaStore = new HashMap<>();
    private Random random = new Random();

    // 生成一个4位数字验证码，返回验证码值 + 可以返回一个 ID 或 token 用于前端提交
    public String generateCapcha(){
        int code = 1000 + random.nextInt(9000);//4位数字
        String catchaValue = String.valueOf(code);
        String catchaKey = UUID.randomUUID().toString();
        captchaStore.put(catchaKey,catchaValue);
        return catchaKey;
    }

    // 检验用户输入的验证码是否正确
    public boolean validateCapcha(String capchaKey,String userInputCode){
        if(userInputCode == null || capchaKey == null){
            return false;
        }
        String value = captchaStore.get(capchaKey);
        //删除验证码（一次性使用）
        captchaStore.remove(capchaKey);
        return userInputCode.equalsIgnoreCase(value);
    }
}
