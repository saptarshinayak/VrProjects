package com.ecommerce.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class OrderHistory {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable=false)
	private Long userId;
	
	@Column(nullable=false)
	private Long orderId;
	
	@Column(nullable=false)
	private String status;
	
	@Column(name="created_at",nullable=false)
	private LocalDateTime createdAt=LocalDateTime.now();
	
	@Column(name="updated_at",nullable=false)
	private LocalDateTime updatedAt=LocalDateTime.now();
	
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

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
