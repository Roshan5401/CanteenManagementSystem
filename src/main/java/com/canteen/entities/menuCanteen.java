package com.canteen.entities;

import java.sql.Date;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Menu_table")
public class menuCanteen {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="FoodID")
	private int ID;
	
	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false)
	private double price;
	
	@Column(nullable=false)
	private String type;
	@Column(nullable=false)
	private Date foodServedDate;
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "food")
	private List<OrderEntity> listOrderEntities;
	
	@Column(nullable=false)
	private boolean Enable;
	



	public boolean isEnable() {
		return Enable;
	}


	public void setEnable(boolean enable) {
		Enable = enable;
	}


	public int getID() {
		return ID;
	}


	public List<OrderEntity> getListOrderEntities() {
		return listOrderEntities;
	}


	public void setListOrderEntities(List<OrderEntity> listOrderEntities) {
		this.listOrderEntities = listOrderEntities;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}




	public Date getFoodServedDate() {
		return foodServedDate;
	}


	public void setFoodServedDate(Date foodServedDate) {
		this.foodServedDate = foodServedDate;
	}


	public void setID(int iD) {
		ID = iD;
	}



	public menuCanteen(int iD, String name, double price, String type, Date foodServedDate,
			List<OrderEntity> listOrderEntities, boolean enable) {
		super();
		ID = iD;
		this.name = name;
		this.price = price;
		this.type = type;
		this.foodServedDate = foodServedDate;
		this.listOrderEntities = listOrderEntities;
		Enable = enable;
	}


	public menuCanteen() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}