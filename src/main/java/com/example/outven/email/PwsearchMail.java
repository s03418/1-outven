package com.example.outven.email;

import java.util.Properties;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class PwsearchMail implements MailServiceInter {

    private static final String SMTP_HOST = "smtp.naver.com";
    private static final int SMTP_PORT = 465;
    private static final String SMTP_USERNAME = "iustotest@naver.com"; // 네이버 이메일 계정
    private static final String SMTP_PASSWORD = "!m123m456m789"; // 네이버 이메일 계정 비밀번호

    @Override
    public String sendSimpleMessage(String to) throws MessagingException {
        String ePw = createKey(); // 랜덤 인증번호 생성

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.enable", "true"); // SSL 사용 설정
        props.put("mail.smtp.ssl.trust", SMTP_HOST); // 신뢰할 수 있는 호스트 설정

        // 세션 생성 시 인증 정보 설정
        Session session = Session.getInstance(props,
            new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
                }
            });

        MimeMessage message = new MimeMessage(session);
        String fromEmail = "iustotest@naver.com"; // 보내는 사람 이메일 주소
        String fromName = "아웃벤 고객센터"; // 보내는 사람 이름
        try {
			message.setFrom(new InternetAddress(fromEmail, fromName));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} // 보내는 사람 주소와 이름 설정
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to)); // 받는 사람 주소
        message.setSubject("비밀번호찾기 이메일 인증"); // 제목

        // HTML 형식의 이메일 본문
        String content = "<div style='margin:50px;'>";
        content += "<h1>안녕하세요</h1>";
        content += "<h1>다양한 정보를 얻을 수 있는 아웃벤입니다</h1>";
        content += "<br>";
        content += "<p>아래 코드를 비밀번호 찾기 창으로 돌아가 입력해주세요<p>";
        content += "<br>";
        content += "<p>항상 감사합니다!<p>";
        content += "<br>";
        content += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        content += "<h3 style='color:blue;'>비밀번호 찾기에 필요한 인증 코드입니다.</h3>";
        content += "<div style='font-size:130%'>";
        content += "CODE : <strong>" + ePw + "</strong><div><br/>"; // 메일에 인증번호 넣기
        content += "</div>";
        
        // 이메일 본문 설정
        message.setContent(content, "text/html; charset=utf-8");

        Transport.send(message); // 메일 전송

        return ePw; // 메일로 보냈던 인증 코드를 서버로 반환
    }


    // 랜덤 인증 코드 생성
    @Override
    public String createKey() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int randInt = random.nextInt(62);
            char randChar = (randInt < 26) ? (char) ('a' + randInt)
                    : (randInt < 52) ? (char) ('A' + randInt - 26)
                    : (char) ('0' + randInt - 52);
            sb.append(randChar);
        }
        return sb.toString();
    }

    @Override
    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SMTP_USERNAME));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject("아웃벤 비밀번호 찾기 인증번호 입니다.");

        String content = "http://localhost:8080/index"; // HTML content for your email
        message.setContent(content, "text/html; charset=utf-8");

        return message;
    }
}
