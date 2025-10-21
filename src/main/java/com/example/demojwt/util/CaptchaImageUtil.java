package com.example.demojwt.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CaptchaImageUtil {

    /**
     * 根据验证码文本生成一张图片（BufferedImage）
     *
     * @param captchaText 验证码内容，如 "1234"
     * @return BufferedImage 图片对象
     */
    public static BufferedImage generateCaptchaImage(String captchaText) {
        int width = 120;
        int height = 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();

        // 设置背景色（白色）
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 设置字体和颜色
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(Color.BLUE);

        // 绘制验证码文本（居中显示，简单处理）
        int x = 20;
        int y = 30;
        g.drawString(captchaText, x, y);

        // 可选：添加干扰线（防机器识别）
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < 3; i++) {
            int x1 = (int) (Math.random() * width);
            int y1 = (int) (Math.random() * height);
            int x2 = (int) (Math.random() * width);
            int y2 = (int) (Math.random() * height);
            g.drawLine(x1, y1, x2, y2);
        }

        g.dispose(); // 释放资源
        return image;
    }

    /**
     * 将 BufferedImage 转为 JPEG 格式的字节数组
     */
    public static byte[] toJpegBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }
}