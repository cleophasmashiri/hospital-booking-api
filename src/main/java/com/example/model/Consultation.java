package com.example.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //https://stackoverflow.com/questions/24994440/no-serializer-found-for-class-org-hibernate-proxy-pojo-javassist-javassist
public class Consultation {

	public Consultation() {
		
	}
	public Consultation(String decision, String comment) {
		this.decision = decision;
		this.comment = comment;
	}
	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	@JoinColumn(name="booking_id")
	@JsonBackReference
	private Booking booking;
	private String decision;
	private String comment;
	private Date DateCreated;
	
}
