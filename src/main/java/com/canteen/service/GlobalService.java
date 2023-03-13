package com.canteen.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.canteen.entities.GlobalEmployees;
import com.canteen.repository.GlobalRepository;

@Component
public class GlobalService {

	@Autowired
	private GlobalRepository globalRepository;
	
	public GlobalEmployees addemployee(GlobalEmployees globalemployee) {
		// TODO Auto-generated method stub
		this.globalRepository.save(globalemployee);
		return globalemployee;
	}
	
	public GlobalService(GlobalRepository globalRepository) {
		super();
		this.globalRepository = globalRepository;
	}
	
}
