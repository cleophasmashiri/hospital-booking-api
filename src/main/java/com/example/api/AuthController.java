package com.example.api;

import java.util.List;
import java.util.stream.Collectors;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.model.Credential;

@RequestMapping("auth/")
@RestController
@CrossOrigin
public class AuthController {

	@Autowired
	IdentityService identityService;
	
	@RequestMapping("login")
	public ResponseEntity<List<String>> login(@RequestBody Credential credential) {
		if(identityService.checkPassword(credential.getEmail(), credential.getPassword())) {
			List<Group> roles = identityService.createGroupQuery().groupMember(credential.getEmail()).groupType("security-role").list();
			if(roles == null) {
				return new ResponseEntity<List<String>>(HttpStatus.UNAUTHORIZED);
			}
			//TODO to clean it up
			return new ResponseEntity<List<String>>(roles.stream().map(r->r.getName()).collect(Collectors.toList()), HttpStatus.OK);
		}
		return new ResponseEntity<List<String>>(HttpStatus.UNAUTHORIZED);
	}
	
	@RequestMapping("register")
	public ResponseEntity<Void> register(@RequestBody Credential credential) {
		User user = identityService.newUser(credential.getEmail());
		user.setEmail(credential.getEmail());
		user.setFirstName(credential.getFirstName());
		user.setLastName(credential.getFirstName());
		user.setPassword(credential.getPassword());
		identityService.saveUser(user);
		credential.getRoles().stream().forEach(r -> identityService.createMembership(credential.getEmail(), r));
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
