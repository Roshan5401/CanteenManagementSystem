package com.canteen.entities;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="Canteen_Users")
public class CanteenUsers {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@NotNull
	//Column can not be null
	private int id;

	@Column(nullable = false)
	private String name;
	
	
	//this is for the email id verification purpose
	//it will verify if the email ends with nrifintech.com  OR   trainee.nrifintech.com
	@Pattern(regexp = "^[\\w-\\.]+@[nrifintech|trainee.nrifintech]\\.com$", message = "Invalid email address")
	@NotNull
	@Column(unique = true)
	private String email;

	
	
	//The ^ and $ characters are used to specify the start and end of the string, .* is used to match any character zero or more times
	//above comment is given for @Pattern
	
	@Column(name = "user_password", nullable = false, length = 60)
	@Size(min = 8, max = 60, message = "Password length must be between 8 and 60 characters")         
	private String password;

	
	@Column(nullable = false)
	private String role;
	
	@Column(length = 10, nullable = false)
	private BigInteger phone;
	
	private Double wallet;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "canteenUsers")
	private List<OrderEntity> orders=new ArrayList<>();
	
	@OneToMany
	private List<menuCanteen> cart;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public BigInteger getPhone() {
		return phone;
	}
	public void setPhone(BigInteger phone) {
		this.phone = phone;
	}
	
	
	
	
	
	public List<menuCanteen> getCart() {
		return cart;
	}
	public void setCart(List<menuCanteen> cart) {
		this.cart = cart;
	}
	public List<OrderEntity> getOrders() {
		return orders;
	}
	public void setOrders(List<OrderEntity> orders) {
		this.orders = orders;
	}
	public Double getWallet() {
		return wallet;
	}
	public void setWallet(Double wallet) {
		this.wallet = wallet;
	}
	public CanteenUsers() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CanteenUsers(@NotNull int id, String name,
			@Pattern(regexp = "^[\\w-\\.]+@[nrifintech|trainee.nrifintech]\\.com$", message = "Invalid email address") @NotNull String email,
			@Size(min = 8, max = 60, message = "Password length must be between 8 and 60 characters") String password,
			String role, BigInteger phone, Double wallet, List<OrderEntity> orders, List<menuCanteen> cart) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.phone = phone;
		this.wallet = wallet;
		this.orders = orders;
		this.cart = cart;
	}
	@Override
	public String toString() {
		return "CanteenUsers [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role="
				+ role + ", phone=" + phone + ", wallet=" + wallet + ", orders=" + orders + ", cart=" + cart + "]";
	}
	
	

}
