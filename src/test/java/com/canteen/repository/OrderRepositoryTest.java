package com.canteen.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.canteen.entities.CanteenUsers;
import com.canteen.entities.OrderEntity;
import com.canteen.entities.menuCanteen;

@SpringBootTest
class OrderRepositoryTest {

	@Mock
	OrderRepository orderrepo;

	@Test
	void testfindByStatus() {

		menuCanteen menu = new menuCanteen();

		CanteenUsers canteen = new CanteenUsers();

		java.sql.Date d = new java.sql.Date(2023 - 03 - 13);

		OrderEntity order = new OrderEntity(1, canteen, menu, d, 4, 233, "Delivered", "Perfect",4);

		orderrepo.save(order);

		LocalDate ld = d.toLocalDate();

		List<OrderEntity> oe = orderrepo.findByStatus("Delivered");
		verify(orderrepo).findByStatus("Delivered");
		

		List<OrderEntity> ord = orderrepo.findByStatusAndOrderDate("Delivered", ld);
		verify(orderrepo).findByStatusAndOrderDate("Delivered", ld);

		OrderEntity ordr = orderrepo.findByOrderId(1);
		verify(orderrepo).findByOrderId(1);

	}
}
