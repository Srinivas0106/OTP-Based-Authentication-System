package com.example.demo.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Token;
import com.example.demo.entities.Users;
import com.example.demo.repositories.TokenRepository;
import com.example.demo.repositories.UserRepository;

@Service
public class AuthService {

	UserRepository userRepository;
	TokenRepository tokenRepository;
	JavaMailSender mailSender;

	public AuthService(UserRepository userRepository, TokenRepository tokenRepository, JavaMailSender mailSender) {
		this.userRepository = userRepository;
		this.tokenRepository = tokenRepository;
		this.mailSender = mailSender;
	}

	public String verifyUser(String username, String password) {
		Users user = userRepository.findByUsername(username);

		if (user != null && password.equals(user.getPassword())) {
			// Logic of sending a mail to user with OTP
			Random random = new Random();
			int randomotp = random.nextInt(999999);
			String otp = String.format("%06d", randomotp);
			// storing otp

			Token token = new Token();
			token.setUser(user);
			token.setOtp(otp);
			token.setCreatedAt(LocalDateTime.now());
			tokenRepository.save(token);

			// send email
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(user.getEmail());
			message.setSubject("Your OTP Code");
			message.setText("Your OTP code is: " + otp);
			mailSender.send(message);

			return "success";
		} else {
			return "Failure";
		}
	}
	
	public boolean verifyOtp(String otp) {
		Token token = tokenRepository.findByOtp(otp);
		
		if(token != null) {
			return true;
		} else {
			return false;
		}
	}
//
//	public String verifyOtp(String otp) {
//		Token token = tokenRepository.findByOtp(otp);
//		if(otp != null) {
//			if(ChronoUnit.MINUTES.between(token.getCreatedAt(), LocalDateTime.now())>1) {
//				tokenRepository.delete(token);
//				return "Expired";
//			}
//		} else {
//				return "success";
//			}
//		return "Failure";
//	}

}
