package com.example;

import java.util.Date;
import java.util.List;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.subethamail.wiser.Wiser;
import com.example.model.Booking;
import com.example.model.BookingResponse;
import com.example.model.ClaimTaskRequest;
import com.example.model.CompleteTaskRequest;
import com.example.model.Consultation;
import com.example.service.BookingService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HospitalbookingApplicationTests {

	@Autowired
	private TaskService taskService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	BookingService bookingService;

	private Wiser wiser;

	@Before
	public void setup() {
		wiser = new Wiser();
		wiser.setPort(1025);
		wiser.start();
	}

	@After
	public void cleanUp() {
		wiser.stop();
	}

	@Test
	public void makeBookingNurseToDoctorToPharmacyPath() {

		Booking booking = new Booking();
		booking.setEmail("patient1@zamayi.com");
		booking.setReason("Sick");
		booking.setRequiredDate(new Date());
		String instanceId = bookingService.makeBooking(booking);
		System.out.println(instanceId);

		Task nurseTask = taskService.createTaskQuery().processInstanceId(instanceId).taskCandidateGroup("nurses")
				.singleResult();
		Assert.assertEquals("Nurse Consultation", nurseTask.getName());

		// verify confirmation email
		//TODO set email for testing Assert.assertEquals(1, wiser.getMessages().size());

		bookingService.claimTask(new ClaimTaskRequest(nurseTask.getId(), "nurse1@zamayi.com"));
		Task nurseTaskClaimed = taskService.createTaskQuery().processInstanceId(instanceId)
				.taskAssignee("nurse1@zamayi.com").singleResult();
		
		List<BookingResponse> mybookings = bookingService.bookingList("patient1@zamayi.com");
		
		Assert.assertEquals(1, mybookings.size());
		System.out.println("mybookings[0].toString");
		System.out.println(mybookings.get(0).toString());
		
		Assert.assertEquals("Nurse Consultation", nurseTaskClaimed.getName());

		bookingService.completeTask(new CompleteTaskRequest(nurseTaskClaimed.getId(), new Consultation("ReferToDoctor", "Nurse Comments"), booking.getId(), "nurses"));
		Task doctorTask = taskService.createTaskQuery().processInstanceId(instanceId).taskCandidateGroup("doctors")
				.singleResult();
		Assert.assertEquals(doctorTask.getName(), "Doctor Consultation");

		bookingService.claimTask(new ClaimTaskRequest(doctorTask.getId(), "doctor1@zamayi.com"));

		doctorTask = taskService.createTaskQuery().taskId(doctorTask.getId()).singleResult();
		Assert.assertEquals(doctorTask.getAssignee(), "doctor1@zamayi.com");

		bookingService.completeTask(new CompleteTaskRequest(doctorTask.getId(),  new Consultation("ReferToPharmacy", "Doctor Comments"), booking.getId(), "doctors"));
		Task pharmacyTask = taskService.createTaskQuery().processInstanceId(instanceId).taskCandidateGroup("pharmacists")
				.singleResult();
		Assert.assertEquals(pharmacyTask.getName(), "Pharmacy Prescription");
		bookingService.claimTask(new ClaimTaskRequest(pharmacyTask.getId(), "pharmacist1@zamayi.com"));
		pharmacyTask = taskService.createTaskQuery().taskId(pharmacyTask.getId()).singleResult();
		Assert.assertEquals(pharmacyTask.getAssignee(), "pharmacist1@zamayi.com");
		bookingService.completeTask(new CompleteTaskRequest(pharmacyTask.getId(),  new Consultation("Completed", "Pharmacist Comments"), booking.getId(), "pharmacists"));

		Assert.assertEquals(1, historyService.createHistoricProcessInstanceQuery().processInstanceId(instanceId).finished().count());

	}

	@Test
	public void makeBookingNurseToPharmacyPath() {

	}

}
