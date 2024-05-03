package com.example.outven.email;

import java.io.UnsupportedEncodingException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface MailServiceInter {

	MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException;
	String createKey();
	String sendSimpleMessage(String to) throws Exception;

}
