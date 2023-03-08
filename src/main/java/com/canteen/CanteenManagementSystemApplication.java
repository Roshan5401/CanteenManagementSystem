package com.canteen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@SpringBootApplication
public class CanteenManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CanteenManagementSystemApplication.class, args);
		
	}
//	public void removeVerificationMessageFromSession() {
//        try {
//            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//            HttpSession session = request.getSession();
//            session.removeAttribute("verificationMessage");
//        } catch (RuntimeException ex) {
//            log.error("No Request: ", ex);
//        }
//    }

}
