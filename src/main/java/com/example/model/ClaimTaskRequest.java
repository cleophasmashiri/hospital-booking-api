package com.example.model;

import lombok.Data;

@Data
public class ClaimTaskRequest {
	public ClaimTaskRequest() {
	}
	public ClaimTaskRequest(String taskId, String userId) {
		this.taskId = taskId;
		this.userId = userId;
	}
	private String taskId;
	private String userId;

}
