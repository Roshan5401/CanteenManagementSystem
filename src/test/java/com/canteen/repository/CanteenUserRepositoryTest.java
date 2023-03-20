package com.canteen.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.canteen.entities.CanteenUsers;

@SpringBootTest
class CanteenUserRepositoryTest {

	private CanteenUserRepository canteenrepo=Mockito.mock(CanteenUserRepository.class); // instance variable

	BigInteger bg = new BigInteger("1234567890");

	private CanteenUsers user=new CanteenUsers(4, "diana", "p@nrifintech.com", "1234", "ROLE_USER", bg, 0.0);

	@Test

	public void testSaveUser() {

	Mockito.when(canteenrepo.save(user)).thenReturn(user);

	CanteenUsers savedUser = canteenrepo.save(user);

	assertEquals(user, savedUser);

	}

	@Test

	public void testFindByEmail() {

	String email = "p@nrifintech.com";

	Mockito.when(canteenrepo.findByEmail(email)).thenReturn(user);

	CanteenUsers actualUser = canteenrepo.findByEmail(email);

	assertEquals(user, actualUser);

	}

	}