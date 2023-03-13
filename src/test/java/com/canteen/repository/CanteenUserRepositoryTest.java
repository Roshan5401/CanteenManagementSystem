package com.canteen.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.canteen.entities.CanteenUsers;

@SpringBootTest
class CanteenUserRepositoryTest {

	@Mock
	private CanteenUserRepository canteenrepo;

	@Test
	void testfindByEmail() {

		BigInteger bg = new BigInteger("1234567890");

		CanteenUsers cu = new CanteenUsers(4, "diana", "p@nrifintech.com", "1234", "ROLE_USER", bg, 0.0);
		this.canteenrepo.save(cu);

		CanteenUsers cd = canteenrepo.findByEmail("p@nrifintech.com");
		verify(canteenrepo).findByEmail("p@nrifintech.com");

	}

}
