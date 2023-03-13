package com.canteen.entities;



import javax.validation.constraints.Pattern;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="GlobalEmployeesTable")
public class GlobalEmployees {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	
	@Column(name="SL_ID")
	private int id;
	
	@Pattern(regexp = "^(.+)+@nrifintech", message = "Invalid email address")
	@Column(name="EMAIL_ID",nullable = false)
	private String email;
	
	
	
	public GlobalEmployees(int id, String email) {
		super();
		this.id = id;
		this.email = email;
	}
	public GlobalEmployees() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	@Override
	public String toString() {
		return "globalEmployees [id=" + id + ", email=" + email + "]";
	}
	
	
	
	
}
