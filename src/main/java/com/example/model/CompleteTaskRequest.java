package com.example.model;

import lombok.Data;

@Data
public class CompleteTaskRequest {
	

	public CompleteTaskRequest(String taskId, Consultation consultation, long bookingId, String consultantRole) {
		this.taskId = taskId;
		this.consultation = consultation;
		this.bookingId = bookingId;
		this.consultantRole = consultantRole;
	}	
	
	public CompleteTaskRequest() {
	}
	
	private String taskId;
	private Consultation consultation;
	private String consultantRole; 
	private long bookingId;
}
