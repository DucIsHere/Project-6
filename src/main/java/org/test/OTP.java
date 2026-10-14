package org.test;

import jakarta.mail.Session;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.Properties;

public class OTP {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SENDER_EMAIL = "your_email@gmail.com";
    private static final String SENDER_PASSWORD = "your_password_app";

    public static String generate6DigitOtp() {
        SecureRandom random = new SecureRandom();
        int number = random.nextInt(1000000);
        return String.format("%06d", number);
    }

    public static void  sendOtpMail(String recipientEmail) {
        String otpCode = generate6DigitOtp();

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session otp = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(otp);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Mã OTP Xác Thực Tài Khoản");
            message.setText("Mã OTP của bạn là: " + otpCode + ". Hiệu lực trong 5 phút.");

            Transport.send(message);
            System.out.println("Đã gửi OTP " + otpCode + " tới: " + recipientEmail);
        } catch (MessagingException e) {
            System.err.println("Lỗi gửi email: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            String recipientEmail = args[0];
            System.out.println("[JAVA OTP SERVICE] Đang xử lý gửi mail cho: " + recipientEmail);
            sendOtpMail(recipientEmail);
        } else {
            System.out.println("[JAVA ERROR] Không nhận được địa chỉ email từ Python!");
        }
    }
}