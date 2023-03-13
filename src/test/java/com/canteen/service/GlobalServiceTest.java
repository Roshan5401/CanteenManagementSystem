package com.canteen.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.canteen.entities.GlobalEmployees;
import com.canteen.repository.GlobalRepository;

@SpringBootTest
class GlobalServiceTest {

	@Mock
	private GlobalRepository globalrepository;

	private GlobalService globalservice;

	@BeforeEach
	void setup() {
		this.globalservice = new GlobalService(this.globalrepository);
	}

	@Test
	void testaddEmployee() {
		GlobalEmployees ge = new GlobalEmployees(1, "h@nrifintech.com");
		GlobalEmployees employee = globalservice.addemployee(ge);
		verify(globalrepository).save(ge);

	}

}
