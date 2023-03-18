package com.canteen.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.canteen.entities.OrderEntity;
import com.canteen.entities.menuCanteen;

@SpringBootTest
class MenuRepositoryTest {

	private MenuRepository menurepo=Mockito.mock(MenuRepository.class);

	List<OrderEntity> orderentity = new ArrayList<OrderEntity>();

	double price = 20;

	String str = "2023-03-02";

	Date dt = Date.valueOf(str);

	int a = 1;

	boolean b = (a == 1);

	menuCanteen menu = new menuCanteen(1,"shreya",price,"Veg",dt,orderentity,b,"img");

	@Test

	public void testSaveUser() {

	Mockito.when(menurepo.save(menu)).thenReturn(menu);

	menuCanteen savedUser = menurepo.save(menu);

	// System.out.println(savedUser);

	assertEquals(menu, savedUser);

	}

	@Test

	void test() {

	//this.menurepository.save(menucanteen);

	menuCanteen menuc = menurepo.findById(1);

	verify(menurepo).findById(1);

	List<menuCanteen> menucn = new ArrayList<menuCanteen>();

	menucn.add(menu);

	List<menuCanteen> m = menurepo.getFoodByEnable(b);

	verify(menurepo).getFoodByEnable(b);

	}

	}
