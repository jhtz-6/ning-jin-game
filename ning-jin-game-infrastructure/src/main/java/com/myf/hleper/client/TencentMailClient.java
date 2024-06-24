package com.myf.hleper.client;

import com.alibaba.fastjson.JSON;
import com.myf.hleper.model.dto.TencentMailDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @Author: myf
 * @CreateTime: 2024-06-23  14:53
 * @Description: TencentMailClient
 */
@Slf4j
@Component
public class TencentMailClient {

    @Autowired
    private ConfigurationClient configurationClient;

    public boolean sendMailToSelf(TencentMailDTO tencentMailDTO) {
        tencentMailDTO.setTargetMailAddress(configurationClient.getValueByKey(ConfigurationClient.TENCENT_MAIL_USER_NAME));
        return sendMailToOther(tencentMailDTO);
    }

    public boolean sendMailToOther(TencentMailDTO tencentMailDTO) {
        if (StringUtils.isBlank(tencentMailDTO.getTargetMailAddress())) {
            return false;
        }
        final String username = configurationClient.getValueByKey(ConfigurationClient.TENCENT_MAIL_USER_NAME);
        // 创建会话对象
        try {
            // 创建邮件消息
            MimeMessage message = new MimeMessage(getSession(username));
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(tencentMailDTO.getTargetMailAddress()));
            message.setSubject(tencentMailDTO.getSubject());
            message.setText(tencentMailDTO.getText());
            // 发送邮件
            Transport.send(message);
        } catch (MessagingException mex) {
            log.error("sendMailToSelf.error.tencentMailDTO:{}", JSON.toJSONString(tencentMailDTO), mex);
            return false;
        }
        return true;
    }

    private Session getSession(String username) {
        final String password = configurationClient.getValueByKey(ConfigurationClient.TENCENT_MAIL_PASSWORD);
        // 设置邮件服务器的属性
        Properties props = new Properties();
        initProperty(props);
        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    private void initProperty(Properties props) {
        props.put("mail.smtp.host", "smtp.qq.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.socketFactory.port", "465");
    }
}
