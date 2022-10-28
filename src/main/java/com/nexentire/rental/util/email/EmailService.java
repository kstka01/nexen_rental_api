package com.nexentire.rental.util.email;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;
	private String from = "nextlevel@nexentire.com";
	
	public EmailService() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean sendEmail(String to, String subject, String img, String attatchFilePath, String attatchFileNm) {
		
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "EUC-KR");
			
			messageHelper.setTo(to);
			messageHelper.setFrom(from);
			messageHelper.setSubject(subject);
			
			//messageHelper.setText(text);
			messageHelper.setText("<html><body><img src='cid:identifier1234'></body></html>", true);
			
			FileSystemResource res = new FileSystemResource(new File(img));
			messageHelper.addInline("identifier1234", res);
			
			FileSystemResource file = new FileSystemResource(new File(attatchFilePath+"/"+attatchFileNm));
			messageHelper.addAttachment(attatchFileNm, file);
			
			javaMailSender.send(message);
		} catch(Exception e){
			return false;
		}
		
		return true;
	}

}
