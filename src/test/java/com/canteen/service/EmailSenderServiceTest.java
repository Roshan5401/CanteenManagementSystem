package com.canteen.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;



@SpringBootTest
class EmailSenderServiceTest {

	
		 JavaMailSender mailSender=Mockito.mock(JavaMailSender.class);
			EmailSenderService emailservice=Mockito.mock(EmailSenderService.class) ;
			
			@BeforeEach

			void setup() {

				this.emailservice = new EmailSenderService(this.mailSender);

			}

			

			@Test
			void testsendEmail() {
				 
				
				String email ="shreyara@trainee.nrifintech.com";
				String sub = "Notif";
				String body ="You are receiving from caanteen system ";

				
				  try
				    {
						emailservice.sendEmail(email,sub,body);
				        return; 
				    }
				    catch (Exception ex)
				    {
				        Assert.fail(ex.getMessage());
				    }
			 	

		}
	}


