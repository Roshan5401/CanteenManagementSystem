package com.canteen.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.canteen.entities.OrderEntity;
import com.canteen.entities.menuCanteen;

@SpringBootTest
class MenuRepositoryTest {

	@Mock
	private MenuRepository menurepository;

	@Test
	void test() {

		List<OrderEntity> orderentity = new ArrayList<OrderEntity>();

		menuCanteen menucanteen = new menuCanteen();

		menucanteen.setID(1);
		menucanteen.setName("shreya");
		menucanteen.setPrice(23);
		menucanteen.setType("Veg");

		String str = "2023-03-02";
		Date dt = Date.valueOf(str);

		menucanteen.setFoodServedDate(dt);
		menucanteen.setListOrderEntities(orderentity);

		int a = 1;
		boolean b = (a == 1);
		menucanteen.setEnable(b);
		this.menurepository.save(menucanteen);

		menuCanteen menu = menurepository.findById(1);
		verify(menurepository).findById(1);

		List<menuCanteen> menuc = new ArrayList<menuCanteen>();
		menuc.add(menucanteen);

		List<menuCanteen> m = menurepository.getFoodByEnable(b);
		verify(menurepository).getFoodByEnable(b);

	}
}
