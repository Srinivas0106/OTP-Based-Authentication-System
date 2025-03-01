package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.services.AuthService;

@Controller
@RequestMapping("/api")
public class UserController {
	
	AuthService authService;
	
	public UserController(AuthService authService) {
		this.authService = authService;
	}
	
	@GetMapping("/")
	public String home() {
		return "login";
	}
	
	@PostMapping("/auth/login")
	public String login(@RequestParam("un") String username , @RequestParam("up") String password)  {
		
		String result = authService.verifyUser(username, password);
		if(result.equalsIgnoreCase("Success")) {
			return "verify";
		}  else {
		return "login";
		}
	}
	
	@PostMapping("/auth/verify")
	public String verifyOtp(@RequestParam("otp") String otp) {
		
		boolean result = authService.verifyOtp(otp);
		if(result) {
			return "success";
		} else {
			return "verify";
		}
	}
}
