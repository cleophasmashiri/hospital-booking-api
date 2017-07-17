package com.example.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class BookingResponse {

	public BookingResponse() {
	}
	
	public BookingResponse(
			String processInstanceId,
			String patientUserId, 
			long bookingId, 
			String name,
			String description,
			boolean isComplete,
			String status,
			String reason,
			Date requiredDate) {
		this.processInstanceId = processInstanceId;
		this.patientUserId = patientUserId;
		this.bookingId = bookingId;
		this.name = name;
		this.description = description;
		this.isComplete = isComplete;
		this.status = status;
		this.reason = reason;
		this.requiredDate = requiredDate;
	}
	
	private String processInstanceId;
	private String patientUserId;
	private long bookingId;
	private String name;
	private String description;
	private boolean isComplete;
	private String status;
	private String reason;
	private Date requiredDate;
	private List<TaskResponse> tasks;
	private UserResponse patientUser;
	
	public String toString() {
		return String.format("InstanceId: %s, patientUserId: %s, bookingId: %b, name: %s, "
				+ "description: %s, isComplete: %b, status: %s, reason: %s, requiredDate: %s ", 
				this.processInstanceId,
		this.patientUserId,
		this.bookingId,
		this.name,
		this.description,
		this.isComplete,
		this.status,
		this.reason,
		this.requiredDate);
	}
	
}