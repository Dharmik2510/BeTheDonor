package com.beTheDonor.service;


import com.beTheDonor.entity.Donors;

import org.json.simple.JSONObject;

import org.springframework.stereotype.Service;


@Service
public interface DonorService {

    public Boolean changeStatusOfOrder(JSONObject payload);

    public Boolean storeTotalAmount(JSONObject payload, Long id);

    public Donors getDonationById(Long id);


}
