package com.canteen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.canteen.entities.CanteenUsers;
import com.canteen.repository.CanteenUserRepository;


@Component
public class CanteenService {

	@Autowired
	private CanteenUserRepository canteenUserRepository;
	public CanteenUsers addUserToCanteen(CanteenUsers canteenUser)
	{
		return this.canteenUserRepository.save(canteenUser);
	}
	public CanteenUsers getById(int id)
	{
		return this.canteenUserRepository.getById(id);
	}
	
	
	public CanteenService(CanteenUserRepository canteenUserRepository) {
		super();
		// TODO Auto-generated constructor stub
		this.canteenUserRepository = canteenUserRepository;
	}
	

}
