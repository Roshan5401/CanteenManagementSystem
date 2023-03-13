package com.canteen.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.canteen.entities.OrderEntity;
import com.canteen.repository.OrderRepository;

@Component
public class OrderService {
	@Autowired
	OrderRepository orderRepository;

	public List<OrderEntity> getAllOrders(String status) {
		List<OrderEntity> orders = this.orderRepository.findByStatus(status);
		/*
		 * List<OrderEntity> orders = (List<OrderEntity>)
		 * this.orderRepository.findAll();
		 */
		return orders;

	}

	public List<OrderEntity> getAllOrdersByStatusAndDate(String status, LocalDate date1) {
		List<OrderEntity> orders = this.orderRepository.findByStatusAndOrderDate(status, date1);
		return orders;
	}

	public List<OrderEntity> getAllOrdersByUserId(String status, String userId) {
		int id = Integer.parseInt(userId);
		List<OrderEntity> orders = this.orderRepository.findByStatus(status);
		List<OrderEntity> finalOrders = orders.stream().filter(order -> order.getCanteenUsers().getId() == id)
				.collect(Collectors.toList());
		return finalOrders;
	}

	public List<OrderEntity> getAllOrdersByDateAndUserId(LocalDate date, String userId, String status) {
		int id = Integer.parseInt(userId);
		List<OrderEntity> orders = this.orderRepository.findByStatusAndOrderDate(status, date);
		System.out.println(orders);
		List<OrderEntity> finalOrders = orders.stream().filter(order -> order.getCanteenUsers().getId() == id)
				.collect(Collectors.toList());
		System.out.println(finalOrders);
		return finalOrders;
	}

	public void deleteOrder(String orderId) {

		int id = Integer.parseInt(orderId);
		this.orderRepository.deleteById(id);
		System.out.println("Successfully Deleted");
	}

	public OrderEntity getbyOrderId(int id) {
		OrderEntity order = this.orderRepository.findByOrderId(id);
		return order;
	}

	public OrderService(OrderRepository orderRepository) {

		this.orderRepository = orderRepository;
	}
}
