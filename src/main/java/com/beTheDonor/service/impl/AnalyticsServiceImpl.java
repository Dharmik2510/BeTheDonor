package com.beTheDonor.service.impl;

import java.util.List;

import com.beTheDonor.repository.DonorRepository;
import com.beTheDonor.repository.PatientRepository;
import com.beTheDonor.repository.RiderRepository;
import com.beTheDonor.service.AnalyticsService;
import com.beTheDonor.entity.Donors;
import com.beTheDonor.entity.Patients;
import com.beTheDonor.entity.Riders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;



@Service
public class AnalyticsServiceImpl implements AnalyticsService {
	
	private PatientRepository patientRepository;
	private DonorRepository donorRepository;
	private RiderRepository riderRepository;


	public AnalyticsServiceImpl(PatientRepository patientRepository, DonorRepository donorRepository, RiderRepository riderRepository) {
		this.patientRepository = patientRepository;
		this.donorRepository = donorRepository;
		this.riderRepository = riderRepository;
	}

	@Override
	public List<Patients> getAllHelpedPatients() {
		return patientRepository.findAllByIshelped(true);
	}

	@Override
	public List<Donors> getAllDonorsWhoHelped() {
		return donorRepository.findAllByHelpDone(true);
	}

	@Override
	public List<Riders> getAllRidersWhoDeliver() {
		return riderRepository.findAllByDelivery(true);
	}

	@Override
	public Double totalAmountOfHelp() {
		return donorRepository.selectTotals();
	}


	@Override
	public void addUser(String firstName, String email, String application_user_role) {
		if (application_user_role.equals("Donor")){
			Donors donors = new Donors();
			donors.setEmailId(email);
			donors.setDonorName(firstName);
			donors.setHelpDone(false);
			donorRepository.save(donors);
		}else if (application_user_role.equals("Patient")){
			Patients patients = new Patients();
			patients.setEmailId(email);
			patients.setPatientName(firstName);
			patients.setIshelped(false);
			patientRepository.save(patients);
		}else if (application_user_role.equals("Rider")){
			Riders riders = new Riders();
			riders.setEmailId(email);
			riders.setDriverName(firstName);
			riders.setDelivery(false);
			riderRepository.save(riders);
		}
	}

	@Override
	public void enableUser(String email, String application_user_role, Long id) {
		if (application_user_role.equals("Donor")){
			List<Donors> donorsList = donorRepository.findAllByEmailId(email);
			if (donorsList.size()>0){
				donorsList.get(0).setStatus(true);
				donorsList.get(0).setDonorId(id);
				donorsList.get(0).setAmount(0D);
			}
			donorRepository.save(donorsList.get(0));
		}else if (application_user_role.equals("Patient")){
			List<Patients> patientList = patientRepository.findAllByEmailId(email);
			if (patientList.size()>0){
				patientList.get(0).setStatus(true);
			}
			patientRepository.save(patientList.get(0));
		}else if (application_user_role.equals("Rider")){
			List<Riders> riderList = riderRepository.findAllByEmailId(email);
			if (riderList.size()>0){
				riderList.get(0).setStatus(true);
			}
			riderRepository.save(riderList.get(0));
		}
	}


	@Override
	public void updateDonor(String email, double amount) {
		List<Donors> donorsList = donorRepository.findAllByEmailId(email);
		if (donorsList.size()>0){
			donorsList.get(0).setHelpDone(true);
			donorsList.get(0).setAmount((donorsList.get(0).getAmount()!=null?donorsList.get(0).getAmount():0)+amount);
		}
		donorRepository.save(donorsList.get(0));
	}

	@Override
	public void updatePatient(String name) {
		List<Patients> patientsList = patientRepository.findAllByPatientName(name.split(":")[1]);
		if (patientsList.size()>0){
			patientsList.get(0).setIshelped(true);
		}
		patientRepository.save(patientsList.get(0));
	}

	@Override
	public void updateRider(String email) {
		List<Riders> ridersList = riderRepository.findAllByEmailId(email);
		if (ridersList.size()>0){
			ridersList.get(0).setDelivery(true);
		}
		riderRepository.save(ridersList.get(0));
	}

	@AllArgsConstructor
	@Getter
	@Setter
	@NoArgsConstructor
	public static class PatientRest{
		private Long id;
		private String patientName;
	}

	@AllArgsConstructor
	@Getter
	@Setter
	@NoArgsConstructor
	public static class DonorRest{
		private Long id;
		private String donorName;
		private Double amount;
	}

	@AllArgsConstructor
	@Getter
	@Setter
	@NoArgsConstructor
	public static class RiderRest{
		private Long id;
		private String driverName;
	}

	
	

}
