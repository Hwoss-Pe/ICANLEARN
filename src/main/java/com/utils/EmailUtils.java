package com.utils;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import org.apache.commons.mail.HtmlEmail;


public class EmailUtils {
    public static String sendEmail(String toEmail) {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 90, 4, 100);
        // 验证码值
        String code = lineCaptcha.getCode().toUpperCase();
        try {
            HtmlEmail send = new HtmlEmail();
            //设置发送邮箱的host 默认值
            send.setHostName("smtp.qq.com");
            //配置发送邮箱和邮箱授权码
            send.setAuthentication("2082589127@qq.com","xmrzjgjnbylnbagc");
            //配置发送方
            send.setFrom("2082589127@qq.com");
            //配置接收人
            send.addTo(toEmail);
            //设置邮箱主题
            send.setSubject("邮箱认证");
            //具体的发送消息

            send.setMsg("欢迎使用邮箱验证本系统！您的验证码是："+code);
            send.setCharset("UTF-8");
            send.send();

        }catch (Exception e){
            e.printStackTrace();
        }
        return code;
    }

}
