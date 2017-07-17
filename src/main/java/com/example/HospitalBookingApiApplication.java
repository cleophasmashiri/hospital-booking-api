package com.example;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HospitalBookingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalBookingApiApplication.class, args);
	}
	
	@Bean
	InitializingBean initializeGroupsAndUsers(final IdentityService identityService) {
		return new InitializingBean() {
			
			@Override
			public void afterPropertiesSet() throws Exception {
				
				Group patients = identityService.newGroup("patients");
				patients.setName("patients");
				patients.setType("security-role");
				identityService.saveGroup(patients);
				User patient1 = identityService.newUser("patient1");
				patient1.setEmail("patient1@zamayi.com");
				patient1.setPassword("password");
				identityService.saveUser(patient1);
				identityService.createMembership("patient1", "patients");
				
				Group nurses = identityService.newGroup("nurses");
				nurses.setName("nurses");
				nurses.setType("security-role");
				identityService.saveGroup(nurses);
				User nurse1 = identityService.newUser("nurse1");
				nurse1.setEmail("nurse1@zamayi.com");
				nurse1.setPassword("password");
				identityService.saveUser(nurse1);
				identityService.createMembership("nurse1", "nurses");
				
				Group doctors = identityService.newGroup("doctors");
				doctors.setName("doctors");
				doctors.setType("security-role");
				identityService.saveGroup(doctors);
				User doctor1 = identityService.newUser("doctor1");
				doctor1.setEmail("doctor1@zamayi.com");
				doctor1.setPassword("password");
				identityService.saveUser(doctor1);
				identityService.createMembership("doctor1", "doctors");
				
				Group pharmacists = identityService.newGroup("pharmacists");
				doctors.setName("pharmacists");
				doctors.setType("security-role");
				identityService.saveGroup(pharmacists);
				User pharmacist1 = identityService.newUser("pharmacist1");
				pharmacist1.setEmail("pharmacist1@zamayi.com");
				pharmacist1.setPassword("password");
				identityService.saveUser(pharmacist1);
				identityService.createMembership("pharmacist1", "pharmacists");
				
			}
			
		};
		
	}
}
