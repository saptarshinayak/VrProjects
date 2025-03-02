package com.ecommerce.model;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	private Long userId;
	
	@Column(nullable=false)
	private double totalAmount;
	
	@Column
	private String promoCode;
	
	@Column(nullable=false)
	private double discountAmount=0.0;
	
	@Column(nullable=false)
	private double finalAmount;
	
	@Column(nullable=false)
	private String status;
	
	@Column(nullable=false)
	private String address;
	
	@Column(name="created_at", nullable=false)
	private LocalDateTime createdAt=LocalDateTime.now();

	@Column(name="updated_at",nullable=false)
	private LocalDateTime updatedAt=LocalDateTime.now();;
	
	@Column(name="created_by")
	private String createdBy="admin";
	
	@Column(name="updated_by")
	private String updatedBy="admin";
	
	@Column(name="is_deleted")
	private int isDeleted;
	
	@Column(name="is_archived")
	private int isArchived;

	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getPromoCode() {
		return promoCode;
	}
	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}
	public double getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}
	public double getFinalAmount() {
		return finalAmount;
	}
	public void setFinalAmount(double finalAmount) {
		this.finalAmount = finalAmount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	public int getIsArchived() {
		return isArchived;
	}
	public void setIsArchived(int isArchived) {
		this.isArchived = isArchived;
	}
}
