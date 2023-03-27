package com.canteen.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.canteen.repository.CanteenUserRepository;

@SpringBootTest
class CanteenServiceTest {

	@Mock
	private CanteenUserRepository canteenrepo;

	private CanteenService canteenservice;

	@BeforeEach
	void setup() {
		this.canteenservice = new CanteenService(this.canteenrepo);
	}

	@Test
	void testgetById() {
		canteenservice.getById(1);
		verify(canteenrepo).getById(1);
	}

}
