package com.canteen.repository;

import org.springframework.data.repository.CrudRepository;

import com.canteen.entities.GlobalEmployees;


public interface GlobalRepository extends CrudRepository<GlobalEmployees, Integer>{
	
	public GlobalEmployees findByEmail(String email);
}
