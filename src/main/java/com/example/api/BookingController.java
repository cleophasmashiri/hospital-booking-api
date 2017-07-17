package com.example.api;

import java.util.List;

import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.model.Booking;
import com.example.model.BookingResponse;
import com.example.model.ClaimTaskRequest;
import com.example.model.CompleteTaskRequest;
import com.example.service.BookingService;

@RequestMapping("booking")
@RestController
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@CrossOrigin
	@RequestMapping(value="/new", method = RequestMethod.POST)
	public ResponseEntity<Void> makeBooking(@RequestBody Booking booking) {
		System.out.println("calling make booking");
		bookingService.makeBooking(booking);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value="/details/{processInstanceId}", method = RequestMethod.GET)
	public ResponseEntity<BookingResponse> getBooking(@PathVariable String processInstanceId) {
		return new ResponseEntity<BookingResponse>(bookingService.getBooking(processInstanceId), HttpStatus.OK);
	}
	
	@CrossOrigin
	@RequestMapping(path="/claim",  method = RequestMethod.POST )
	public ResponseEntity<Void> claimTask(@RequestBody ClaimTaskRequest request) {
		bookingService.claimTask(request);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@CrossOrigin
	@RequestMapping(value="/complete",  method = RequestMethod.POST )
	public ResponseEntity<Void> completeTask(@RequestBody CompleteTaskRequest request) {
		bookingService.completeTask(request);//(request);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@CrossOrigin
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public ResponseEntity<List<BookingResponse>> bookingList() {
		return new ResponseEntity<List<BookingResponse>>(bookingService.bookingList(), HttpStatus.OK);
	}
	
	@CrossOrigin
	@RequestMapping(value="/mybookings/{userId}", method=RequestMethod.GET)
	public ResponseEntity<List<BookingResponse>> bookingListByUserId(@PathVariable String userId) {
		return new ResponseEntity<List<BookingResponse>>(bookingService.bookingList(userId), HttpStatus.OK);
	}

}
