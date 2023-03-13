package com.canteen.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.canteen.entities.GlobalEmployees;

@SpringBootTest
class GlobalRepositoryTest {

	@Mock
	private GlobalRepository globalrepository;

	@Test
	void testfindByEmail() {

		GlobalEmployees ge = new GlobalEmployees(1, "a@mail.com");
		globalrepository.save(ge);

		GlobalEmployees ag = globalrepository.findByEmail("a@mail.com");
		verify(globalrepository).findByEmail("a@mail.com");

	}

}
