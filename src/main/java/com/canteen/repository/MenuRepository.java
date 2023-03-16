package com.canteen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.canteen.entities.menuCanteen;

public interface MenuRepository extends JpaRepository<menuCanteen, Integer> {
	public menuCanteen findById(int id);
	
	@Query("select u From menuCanteen u Where u.Enable=:n")
	public List<menuCanteen> getFoodByEnable(@Param("n") boolean val);
	
	@Query("select u From menuCanteen u Where u.name=:n and u.Enable=1")
	public menuCanteen findByName(@Param("n") String foodname);
}
