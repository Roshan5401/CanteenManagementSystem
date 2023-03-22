package com.canteen.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.canteen.entities.CanteenUsers;
import com.canteen.repository.CanteenUserRepository;



public class UserDetailsServiceImpl implements UserDetailsService{
	@Autowired
	CanteenUserRepository repository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		CanteenUsers user = repository.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException("Could not find user");
		}
		CustomerUserDetails customerUserDetails=new CustomerUserDetails(user);
		return customerUserDetails;
	}

}