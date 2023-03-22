package com.canteen.entities;

import java.sql.Date;

import javax.validation.constraints.Null;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_details")
public class OrderEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "order_id")
   private int orderId;
   
   @ManyToOne
   private CanteenUsers canteenUsers;
   

   @ManyToOne 
   private menuCanteen food;

   @Column(name = "order_date")
   private Date orderDate;

   @Column(name = "quantity")
   private int quantity;

   @Column(name = "total_price")
   private double totalPrice;

   @Column(name = "status")
   private String status;

   @Column(name="feedback",length = 1000)
   private String feedback;
   
   @Column(name="rating")
   private Integer rating;
   
public OrderEntity() {
	super();
	// TODO Auto-generated constructor stub
}









public Integer getRating() {
	return rating;
}


public void setRating(Integer rating) {
	this.rating = rating;
}



public CanteenUsers getCanteenUsers() {
	return canteenUsers;
}

public void setCanteenUsers(CanteenUsers canteenUsers) {
	this.canteenUsers = canteenUsers;
}

public menuCanteen getFood() {
	return food;
}

public void setFood(menuCanteen food) {
	this.food = food;
}



public int getQuantity() {
	return quantity;
}

public void setQuantity(int quantity) {
	this.quantity = quantity;
}

public double getTotalPrice() {
	return totalPrice;
}

public void setTotalPrice(double totalPrice) {
	this.totalPrice = totalPrice;
}

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}




public int getOrderId() {
	return orderId;
}



public void setOrderId(int orderId) {
	this.orderId = orderId;
}



public Date getOrderDate() {
	return orderDate;
}



public void setOrderDate(Date orderDate) {
	this.orderDate = orderDate;
}








public String getFeedback() {
	return feedback;
}




public void setFeedback(String feedback) {
	this.feedback = feedback;
}




//public OrderEntity(int orderId, CanteenUsers canteenUsers, menuCanteen food, Date orderDate, int quantity,
//		double totalPrice, String status, String feedback) {
//	super();
//	this.orderId = orderId;
//	this.canteenUsers = canteenUsers;
//	this.food = food;
//	this.orderDate = orderDate;
//	this.quantity = quantity;
//	this.totalPrice = totalPrice;
//	this.status = status;
//	this.feedback = feedback;
//}






//  @Override public String toString() { return "OrderEntity [orderId=" + orderId
//  + ", canteenUsers=" + canteenUsers + ", food=" + food + ", orderDate=" +
//  orderDate + ", quantity=" + quantity + ", totalPrice=" + totalPrice +
//  ", status=" + status + ", feedback=" + feedback + "]"; }





public OrderEntity(int orderId, CanteenUsers canteenUsers, menuCanteen food, Date orderDate, int quantity,
		double totalPrice, String status, String feedback, Integer rating) {
	super();
	this.orderId = orderId;
	this.canteenUsers = canteenUsers;
	this.food = food;
	this.orderDate = orderDate;
	this.quantity = quantity;
	this.totalPrice = totalPrice;
	this.status = status;
	this.feedback = feedback;
	this.rating = rating;
}




@Override
public String toString() {
	return "OrderEntity [orderId=" + orderId + ", food=" + food + ", orderDate="
			+ orderDate + ", quantity=" + quantity + ", totalPrice=" + totalPrice + ", status=" + status + ", feedback="
			+ feedback + ", rating=" + rating + "]";
}
 










   
   
}