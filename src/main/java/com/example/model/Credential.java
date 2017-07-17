package com.example.model;

import java.util.List;

import lombok.Data;

@Data
public class Credential {
	private String firstName;
	private String lastName;
	private String password;
	private String email;
	private List<String> roles;
}
