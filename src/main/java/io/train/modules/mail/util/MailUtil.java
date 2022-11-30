package io.train.modules.mail.util;

import io.train.modules.mail.vo.MailVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.internet.InternetAddress;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

/**
 * 邮件工具栏
 * @author: liyajie
 * @version: 1.0
 * @date: 2021/06/29 14:26
 */
@Component
public class MailUtil {

    public static String host;

    public static String port;

    public static String auth;

    public static String username;

    public static String password;

    public static String socketFactoryClass;

    public static String socketFactoryFallback;

    @Value("${spring.mail.host}")
    public  void setHost(String host) {
        this.host = host;
    }

    @Value("${spring.mail.port}")
    public  void setPort(String port) {
        this.port = port;
    }

    @Value("${spring.mail.properties.mail.smtp.auth}")
    public  void setAuth(String auth) {
        this.auth = auth;
    }

    @Value("${spring.mail.username}")
    public  void setUsernam(String username) {
        this.username = username;
    }

    @Value("${spring.mail.password}")
    public  void setPassword(String password) {
        this.password = password;
    }

    @Value("${spring.mail.properties.mail.smtp.socketFactory.class}")
    public  void setSocketFactoryClass(String socketFactoryClass) {
        this.socketFactoryClass = socketFactoryClass;
    }

    @Value("${spring.mail.properties.mail.smtp.socketFactory.fallback}")
    public  void setSocketFactoryFallback(String socketFactoryFallback) {
        this.socketFactoryFallback = socketFactoryFallback;
    }


    /**
     * 发送文本邮件
     *
     * @param mailVo
     */
    public  static void sendSimpleMail(String sender,MailVo mailVo, JavaMailSender javaMailSender) {
        try {
            SimpleMailMessage mailMessage= new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(mailVo.getRecipient());
            mailMessage.setSubject(mailVo.getSubject());
            mailMessage.setText(mailVo.getContent());
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int sendEmail(String sender,MailVo mailVo, JavaMailSender javaMailSender)

    {
        int result=0;
        // 指定发送邮件的主机为smtp.163.com
        //String host = "smtp.163.com";  //163邮件服务器
        // 获取系统属性
        Properties properties = System.getProperties();
        // 设置邮件服务器
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.auth", auth);
        //阿里云服务器禁用25端口，所以服务器上改为465端口
        properties.put("mail.smtp.socketFactory.port", port);
        properties.put("mail.smtp.socketFactory.class", socketFactoryClass);
        properties.setProperty("mail.smtp.socketFactory.fallback", socketFactoryFallback);
        properties.setProperty("mail.smtp.socketFactory.port", port);
        // 获取默认session对象
        Session session = Session.getDefaultInstance(properties,new Authenticator(){
            public PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(username, password);
            }
        });
        try{
            // 创建默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(session);
            // Set From: 头部头字段
            message.setFrom(new InternetAddress(sender));
            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(mailVo.getRecipient()));
            // Set Subject: 头部头字段
            message.setSubject(mailVo.getSubject());
            // 设置消息体
            message.setText(mailVo.getContent());
            // 发送消息
            Transport.send(message);
            result=1;
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
        return result;
    }
}
