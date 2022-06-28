package com.beTheDonor.controller;


import com.beTheDonor.constant.Constants;
import com.beTheDonor.dto.PatientRiderDto;
import com.beTheDonor.model.PatientRiderModel;
import com.beTheDonor.model.ReadyToDeliverModel;
import com.beTheDonor.service.impl.RiderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/riderDashboard")
public class RiderController {
    RiderService riderService;



    @PostMapping("/showSelectedOrders")
    public String selected(@RequestBody JSONObject payload,Model model)
    {
        System.out.println("Hello all");
        Boolean response = riderService.changeStatusOfOrder(payload);
        System.out.println(response);
        return "redirect:/riderDashboard/showSelectedOrders";

    }

    @GetMapping("/showSelectedOrders")
    public String showSelected(Model model)
    {

        List<Long> ids = new ArrayList<>();
        List<ReadyToDeliverModel> ordersList = riderService.getRemaining();
        for (ReadyToDeliverModel readyToDeliverModel:ordersList)
        {
            System.out.println(readyToDeliverModel.getOrder_id().longValue());
            ids.add(readyToDeliverModel.getOrder_id().longValue());
        }
        Double tipPending = riderService.getTips(ids);
        model.addAttribute("orders",ordersList);
        model.addAttribute("tipPending",tipPending);
        System.out.println("tips: " + tipPending);
        return "confirmDelivery";
    }


    @GetMapping("/city")
    public String getRiderDashboard(@RequestParam(value = "cityName", required = false) String cityName, Model model) {
        System.out.println("cityname passed in controller is: " + cityName);
        ArrayList<PatientRiderModel> patientDetails = (ArrayList<PatientRiderModel>) riderService.getByCityName(cityName);
        System.out.println(patientDetails.size());
        ArrayList<PatientRiderDto> patientRiderDtoList = PatientRiderDto.convertToDto(patientDetails);
        System.out.println("Dto size is: " + patientRiderDtoList.size());
        if (patientDetails.isEmpty()) {
            return "redirect:/riderDashboard?noPatient";
        }
        model.addAttribute("cityName", cityName);
        model.addAttribute("patients", patientRiderDtoList);
        return "riderDashboard";
    }

    @GetMapping("/keyword")
    public ResponseEntity<String> getRiders(@RequestParam(value = "q") String query) throws JsonProcessingException {
        System.out.println(query);
        List<String> strings = riderService.getCities().stream().filter(city -> city.toLowerCase().contains(query)).limit(Constants.auto_limit).collect(Collectors.toList());
        System.out.println("Cities are: " + strings);
        ObjectMapper mapper = new ObjectMapper();
        String resp = "";
        resp = mapper.writeValueAsString(strings);
        return new ResponseEntity<String>(resp, HttpStatus.OK);
    }


    @GetMapping("/changeStatus/{oId}")
    @ResponseBody
    public void changeStatus(@PathVariable Long oId)
    {
        riderService.getOrderById(oId);
    }

}
