package com.canteen.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.canteen.entities.CanteenUsers;
import com.canteen.entities.OrderEntity;
import com.canteen.entities.menuCanteen;
import com.canteen.repository.OrderRepository;

@SpringBootTest
class OrderServiceTest {

	OrderRepository orderrepo = mock(OrderRepository.class);

	OrderService orderservice = mock(OrderService.class);

	@BeforeEach

	void setup() {

		this.orderservice = new OrderService(this.orderrepo);

	}

	@Test

	void test() {

		menuCanteen menu = new menuCanteen();

		CanteenUsers canteen = new CanteenUsers();

		java.sql.Date d = new java.sql.Date(2023 - 03 - 12);

		OrderEntity order = new OrderEntity(1, canteen, menu, d, 2, 233, "Delivered", "Good",4);

		orderrepo.save(order);

		List<OrderEntity> orderlist = orderrepo.findByStatus("Delivered");

	
		assertThat(orderservice.getAllOrders("Delivered")).isEqualTo(orderlist);

		LocalDate localDate1 = d.toLocalDate();

		List<OrderEntity> olist = orderrepo.findByStatusAndOrderDate("Delivered", localDate1);

		assertThat(orderservice.getAllOrdersByStatusAndDate("Delivered", localDate1)).isEqualTo(olist);

		OrderEntity oentity = orderrepo.findByOrderId(1);
		// Mockito.when(orderrepo.findByOrderId(1)).thenReturn(order);

		assertThat(orderservice.getbyOrderId(1)).isEqualTo(oentity);

	}

	@Test

	void deletetest() {

		CanteenUsers canteen2 = new CanteenUsers();

		menuCanteen menu2 = new menuCanteen();

		java.sql.Date dd = new java.sql.Date(2023 - 03 - 12);

		OrderEntity order2 = new OrderEntity(2, canteen2, menu2, dd, 3, 300, "Delivered", "okay",5);

		orderrepo.save(order2);

		orderservice.deleteOrder("2");

		verify(orderrepo).deleteById(2);

	}

	@Test

	void getAllOrdersByDateAndUserIdtest() {

		CanteenUsers canteen3 = new CanteenUsers();

		menuCanteen menu3 = new menuCanteen();

		java.sql.Date dds = new java.sql.Date(2023 - 03 - 14);

		OrderEntity order3 = new OrderEntity(3, canteen3, menu3, dds, 3, 300, "Delivered", "fine",2);

		LocalDate dr = dds.toLocalDate();

		orderrepo.save(order3);

		List<OrderEntity> result = orderservice.getAllOrdersByDateAndUserId(dr, "3", "Delivered");

		assertNotNull(result);
		assertThat(orderservice.getAllOrdersByDateAndUserId(dr, "3", "Delivered")).isEqualTo(result);

	}

}
