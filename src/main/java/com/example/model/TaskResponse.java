package com.example.model;

import java.util.Date;

import lombok.Data;

@Data
public class TaskResponse {

	private String taskId;
	private String name;
	private String owner;
	private String assignee;
	private boolean completed;
	private Date startTime;
	private Date endTime;
	private long duration;
	private String comments;
	private Date dateCreated;
	private String decision;
	
}
