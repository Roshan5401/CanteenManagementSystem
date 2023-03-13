package com.canteen.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.canteen.entities.OrderEntity;

public interface OrderRepository extends CrudRepository<OrderEntity, Integer>{
	public List<OrderEntity> findByStatus(String status);
	public List<OrderEntity> findByStatusAndOrderDate(String status , LocalDate date);
	public OrderEntity findByOrderId(int id);
}
