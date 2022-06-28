package com.beTheDonor.controller;

import java.util.ArrayList;
import java.util.List;

import com.beTheDonor.entity.*;
import com.beTheDonor.repository.DonorRepository;
import com.beTheDonor.repository.PatientRepository;
import com.beTheDonor.repository.RiderRepository;
import com.beTheDonor.service.AnalyticsService;
import com.beTheDonor.service.impl.AnalyticsServiceImpl;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {
//	Creating the reference of AnalyticsService
	private AnalyticsService analyticsService;
	private final PatientRepository patientRepository;
	private final RiderRepository riderRepository;
	private final DonorRepository donorRepository;
	final ObjectFactory<HttpSession> httpSessionFactory;

	public AnalyticsController(AnalyticsService analyticsService, PatientRepository patientRepository, RiderRepository riderRepository, DonorRepository donorRepository, ObjectFactory<HttpSession> httpSessionFactory) {
		super();
		this.analyticsService = analyticsService;
		this.patientRepository = patientRepository;
		this.riderRepository = riderRepository;
		this.donorRepository = donorRepository;
		this.httpSessionFactory = httpSessionFactory;
	}
	
//	making call to the api /api/v1/analytics/patients and checking if we are getting success response (200 OK) or not
	@GetMapping("/patients")
	public ResponseEntity<List<AnalyticsServiceImpl.PatientRest>> getAllHelpedPatients(){
		List<Patients> patientsList = analyticsService.getAllHelpedPatients();
		List<AnalyticsServiceImpl.PatientRest> patientRestList = new ArrayList<>();
		for(Patients patients : patientsList){
			patientRestList.add(new AnalyticsServiceImpl.PatientRest(patients.getId(),patients.getPatientName()));
		}
		return new ResponseEntity<>(patientRestList, HttpStatus.OK);
		
	}
//	making call to the api /api/v1/analytics/donors and checking if we are getting success response (200 OK) or not
	@GetMapping("/donors")
	public ResponseEntity<List<AnalyticsServiceImpl.DonorRest>> getAllDonorsWhoHelped(){
		List<Donors> donorsList = analyticsService.getAllDonorsWhoHelped();
		List<AnalyticsServiceImpl.DonorRest> donorRestList = new ArrayList<>();
		for(Donors donors : donorsList){
			donorRestList.add(new AnalyticsServiceImpl.DonorRest(donors.getId(),donors.getDonorName(), donors.getAmount()));
		}
		return new ResponseEntity<>(donorRestList, HttpStatus.OK);
	}
//	making call to the api /api/v1/analytics/riders and checking if we are getting success response (200 OK) or not
	@GetMapping("/riders")
	public ResponseEntity<List<AnalyticsServiceImpl.RiderRest>> getAllRidersWhoDeliver(){
		List<Riders> ridersList = analyticsService.getAllRidersWhoDeliver();
		List<AnalyticsServiceImpl.RiderRest> riderRestList = new ArrayList<>();
		for(Riders riders : ridersList){
			riderRestList.add(new AnalyticsServiceImpl.RiderRest(riders.getId(),riders.getDriverName()));
		}
		return new ResponseEntity<>(riderRestList, HttpStatus.OK);
	}
//	making call to the api /api/v1/analytics/total and checking if we are getting success response (200 OK) or not
	@GetMapping("/total")
	public ResponseEntity<TotalAmount> getAllProducts(){
		TotalAmount totalAmount = new TotalAmount();
		totalAmount.setTotalAmount(analyticsService.totalAmountOfHelp());
		return new ResponseEntity<TotalAmount>(totalAmount, HttpStatus.OK);
		
	}

	@PostMapping("/updatePatientHelp")
	public @ResponseBody Response updatePatients(String patient) {
		analyticsService.updatePatient(patient.toString());
		return new Response(true, "Updated patients");
	}

	@PostMapping("/updateRiderHelp")
	public @ResponseBody Response updateRiders() {
		HttpSession session = httpSessionFactory.getObject();
		String loginUser = session.getAttribute("loginUserEmail").toString();
		analyticsService.updateRider(loginUser.toString());
		return new Response(true, "Updated patients");
	}

	@GetMapping("")
	public String getAnalytics(Model model){
		List<Patients> PatientsModel =patientRepository.findAllByIshelped(true);
		List<Donors> DonorModel = donorRepository.findAllByHelpDone(true);
		List<Riders> RiderModel = riderRepository.findAllByDelivery(true);
		Double totalDonor = analyticsService.totalAmountOfHelp();
		model.addAttribute("patients", PatientsModel);
		model.addAttribute("donors",DonorModel);
		model.addAttribute("riders",RiderModel);
		model.addAttribute("totalAmountHelp", totalDonor);
		model.addAttribute("patientCount", PatientsModel.size());
		model.addAttribute("donorCount", DonorModel.size());
		model.addAttribute("riderCount", RiderModel.size());
		return "analyticsdashboard";
	}

}
