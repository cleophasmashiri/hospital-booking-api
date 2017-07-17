package com.example.service;

import java.util.List;

import com.example.model.Booking;
import com.example.model.BookingResponse;
import com.example.model.ClaimTaskRequest;
import com.example.model.CompleteTaskRequest;

public interface BookingService {

	String makeBooking(Booking booking);

	BookingResponse getBooking(String processInstanceId);

	List<BookingResponse> bookingList();

	void claimTask(ClaimTaskRequest taskRequest);

	void completeTask(CompleteTaskRequest completeTaskRequest);

	List<BookingResponse> bookingList(String userId);

}