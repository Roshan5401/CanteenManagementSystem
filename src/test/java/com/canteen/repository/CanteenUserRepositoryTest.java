package com.canteen.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.canteen.entities.CanteenUsers;
import com.canteen.entities.OrderEntity;

@SpringBootTest
class CanteenUserRepositoryTest {

	private CanteenUserRepository canteenrepo=Mockito.mock(CanteenUserRepository.class); // instance variable

	BigInteger bg = new BigInteger("1234567890");
	int a= 1;
	boolean b =( a== 1);
	
	List<OrderEntity> ordr = new ArrayList<>();

	private CanteenUsers user=new CanteenUsers(4, "diana", "p@nrifintech.com", "1234", "ROLE_USER", bg, 0.0,ordr,b);

	
	
	
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
	
	
	
	@Test
	public void testfindOrdersByEmail() {

		String email = "p@nrifintech.com";

		Mockito.when(canteenrepo.findOrdersByEmail(email)).thenReturn(ordr);

		List<OrderEntity> actual = canteenrepo.findOrdersByEmail(email);

		assertEquals(ordr, actual);

		}
	

	}