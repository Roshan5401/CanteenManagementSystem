package com.canteen.service;

import org.apache.logging.log4j.util.StringBuilderFormattable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

	@Autowired
	private JavaMailSender mailSender;
	
	public void sendEmail(String toEmail,String toSubject,String Body)
	{
		SimpleMailMessage message=new SimpleMailMessage();
		message.setFrom("canteenmangementsystem@gmail.com");
		message.setTo(toEmail);
		message.setSubject(toSubject);
		message.setText(Body);
		mailSender.send(message);
		System.out.println("mail sent Successfully");
	}
	
	public EmailSenderService(JavaMailSender mailSender) {
		this.mailSender=mailSender;
	}
}
