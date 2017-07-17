package com.example.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.model.Booking;
import com.example.model.BookingRepository;
import com.example.model.BookingResponse;
import com.example.model.ConsultationRepository;
import com.example.model.TaskResponse;
import com.example.model.UserResponse;
import com.example.model.ClaimTaskRequest;
import com.example.model.CompleteTaskRequest;
import com.example.model.Consultation;

@Component
public class BookingServiceImpl implements BookingService {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private RuntimeService runtimeSvc;

	@Autowired
	private TaskService taskService;

	@Autowired
	private ConsultationRepository consultationRepository;

	@Autowired
	private HistoryService historyService;

	@Autowired
	private IdentityService identityService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.example.service.BookingService#makeBooking(com.example.model.Booking)
	 */
	@Override
	public String makeBooking(Booking booking) {
		bookingRepository.save(booking);
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("booking", booking);

		/// set patient user id
		variables.put("patientUserId", booking.getEmail());

		ProcessInstance pi = runtimeSvc.startProcessInstanceByKey("hospitalBookingProcess", variables);

		return pi.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.service.BookingService#getBooking(long)
	 */
	@Override
	public BookingResponse getBooking(String processInstanceId) {
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).includeProcessVariables().singleResult();
		BookingResponse bookingResponse = convertToBookingResponse(historicProcessInstance);
		if(bookingResponse==null)
			return null;
		// Add tasks
		bookingResponse.setTasks(getTasks(processInstanceId));
		
		// add patient details
		User user = identityService.createUserQuery().userId(bookingResponse.getPatientUserId()).singleResult();
		if(user!=null) {
			UserResponse u = new UserResponse();
			u.setEmail(user.getEmail());
			u.setFirstName(user.getFirstName());
			u.setLastName(user.getLastName());
			u.setId(user.getId());
			bookingResponse.setPatientUser(u);
		}
		return bookingResponse;
	}

	private List<TaskResponse> getTasks(String processInstanceId) {
		List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
		if (taskList == null || taskList.size() < 1) {
			return null;
		}
		List<TaskResponse> tasks = new ArrayList<TaskResponse>();
		for (HistoricTaskInstance task : taskList) {
			TaskResponse t = new TaskResponse();
			Map<String, Object> variables = task.getProcessVariables();
			if(variables!=null && variables.get("consultation")!=null) {
				Consultation consultation = (Consultation)variables.get("consultation");
				t.setComments(consultation.getComment());
				t.setDateCreated(consultation.getDateCreated());
				t.setDecision(consultation.getDecision());
			}
			t.setTaskId(task.getId());
			t.setName(task.getName());
			t.setOwner(task.getOwner());
			t.setAssignee(task.getAssignee());
			//t.setAssignee);
			if (task.getEndTime() == null) {
				t.setCompleted(false);
			} else {
				t.setCompleted(true);
				t.setStartTime(task.getStartTime());
				t.setEndTime(task.getEndTime());
				t.setDuration(task.getDurationInMillis());
			}
			tasks.add(t);
		}
		return tasks;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.service.BookingService#bookingList()
	 */
	@Override
	public List<BookingResponse> bookingList() {
		List<BookingResponse> bookings = new ArrayList<BookingResponse>();
		List<HistoricProcessInstance> instances = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey("hospitalBookingProcess").includeProcessVariables().list();
		for (HistoricProcessInstance item : instances) {
			bookings.add(convertToBookingResponse(item));
		}
		return bookings;
	}

	@Override
	public List<BookingResponse> bookingList(String userId) {
		List<BookingResponse> bookings = new ArrayList<BookingResponse>();
		List<HistoricProcessInstance> instances = historyService.createHistoricProcessInstanceQuery()
				.processDefinitionKey("hospitalBookingProcess").variableValueEqualsIgnoreCase("patientUserId", userId)
				.includeProcessVariables().list();
		for (HistoricProcessInstance item : instances) {
			bookings.add(convertToBookingResponse(item));
		}

		// List<BookingResponse> x =
		// runtimeSvc.createProcessInstanceQuery().processDefinitionKey("hospitalBookingProcess").variableValueEqualsIgnoreCase("patientUserId",
		// userId)
		// .list().stream().map(b ->
		// convertToBookingResponse(b)).collect(Collectors.toList());
		System.out.println("bookingList  .....");
		// System.out.println(x.get(0).toString());
		return bookings;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.service.BookingService#claimTask(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void claimTask(ClaimTaskRequest taskRequest) {
		taskService.claim(taskRequest.getTaskId(), taskRequest.getUserId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.service.BookingService#completeTask(java.lang.String,
	 * com.example.model.Consultation, java.lang.String, long)
	 */
	@Override
	public void completeTask(CompleteTaskRequest completeTaskRequest) {
		Booking booking = bookingRepository.findOne(completeTaskRequest.getBookingId());
		completeTaskRequest.getConsultation().setDateCreated(new Date());
		completeTaskRequest.getConsultation().setBooking(booking);
		consultationRepository.save(completeTaskRequest.getConsultation());
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(completeTaskRequest.getConsultantRole(), completeTaskRequest.getConsultation());
		taskService.complete(completeTaskRequest.getTaskId(), variables);
	}

	private BookingResponse convertToBookingResponse(HistoricProcessInstance processInstance) {
		//
		if(processInstance==null)
			return null;
		Map<String, Object> variables = processInstance.getProcessVariables();
		Booking booking = (Booking) variables.get("booking");
		return processInstance != null ? new BookingResponse(processInstance.getId(),
				(String) variables.get("patientUserId"), booking.getId(), processInstance.getName(),
				processInstance.getDescription(), processInstance.getEndTime() != null ? true : false, "", // processInstance.getActivityId().toString(),
				booking.getReason(), booking.getRequiredDate()) : null;
	}

}
