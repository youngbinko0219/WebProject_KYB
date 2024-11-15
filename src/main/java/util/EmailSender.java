package util;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailSender {

	private static final String SMTP_HOST = "smtp.gmail.com";
	private static final int SMTP_PORT = 587;
	private static final String SMTP_USERNAME = "tjckdwlq@gmail.com"; // 발신자 이메일 주소
	private static final String SMTP_PASSWORD = "plfqcoupgedzmltv"; // 발신자 이메일 비밀번호 (앱 비밀번호 사용 권장)

	/**
	 * 이메일 전송 메서드
	 * 
	 * @param to      수신자 이메일 주소
	 * @param subject 이메일 제목
	 * @param content 이메일 내용
	 * @return 이메일 전송 성공 여부
	 */
	public static boolean sendEmail(String to, String subject, String content) {
		// SMTP 서버 설정
		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST);
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true"); // TLS 보안 설정

		// 인증 정보 설정
		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
			}
		});

		try {
			// 이메일 메시지 설정
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(SMTP_USERNAME));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setText(content);

			// 이메일 전송
			Transport.send(message);
			System.out.println("이메일 전송 성공: " + to);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			System.out.println("이메일 전송 실패: " + to);
			return false;
		}
	}
}
