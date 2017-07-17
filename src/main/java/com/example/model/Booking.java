package com.example.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Entity
@Data
@Table
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //https://stackoverflow.com/questions/24994440/no-serializer-found-for-class-org-hibernate-proxy-pojo-javassist-javassist
public class Booking {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String email;
	private String reason;
	private Date requiredDate;
	
	@OneToMany(mappedBy = "booking")
	private Set<Consultation> consultations = new HashSet<Consultation>();
	
	public Booking() {
		// for hibernate 
	}
}