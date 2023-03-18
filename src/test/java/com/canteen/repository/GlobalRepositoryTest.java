package com.canteen.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.canteen.entities.GlobalEmployees;

@SpringBootTest
class GlobalRepositoryTest {

	private GlobalRepository globalrepository = Mockito.mock(GlobalRepository.class);

	private GlobalEmployees ge = new GlobalEmployees(1, "a@mail.com");

	@Test

	public void testSaveUser() {

		Mockito.when(globalrepository.save(ge)).thenReturn(ge);

		GlobalEmployees savedUser = globalrepository.save(ge);

		assertEquals(ge, savedUser);

	}

	@Test

	void testfindByEmail() {

		String email = "a@mail.com";

		Mockito.when(globalrepository.findByEmail(email)).thenReturn(ge);

		GlobalEmployees actualUser = globalrepository.findByEmail(email);

		assertEquals(ge, actualUser);

	}

}