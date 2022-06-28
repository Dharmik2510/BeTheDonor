package com.beTheDonor.service.impl;

import com.beTheDonor.entity.ApplicationUser;
import com.beTheDonor.entity.Donors;
import com.beTheDonor.entity.Orders;
import com.beTheDonor.repository.DonorRepository;
import com.beTheDonor.repository.OrderRepository;
import com.beTheDonor.repository.UserRepository;
import com.beTheDonor.service.DonorService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DonorServiceImpl implements DonorService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    DonorRepository donorRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public Boolean changeStatusOfOrder(JSONObject payload) {
        try {
            ArrayList<Long> jsonAddress = (ArrayList) payload.get("orderId");
            for (int i = 0; i < jsonAddress.size(); i++) {
                Orders order;
                Long id;
                id = ((Number) jsonAddress.get(i)).longValue();
                order = orderRepository.getById(id);
                order.setOrderStatus("pending delivery");
                orderRepository.save(order);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public Boolean storeTotalAmount(JSONObject payload, Long id) {
        Donors donor = new Donors();
        boolean donorExists = donorRepository.existsByDonorId(id);
        List<Integer> totalAmount = (List<Integer>) payload.get("donationAmount");
        Integer sum = totalAmount.stream()
                .reduce(0, Integer::sum);
        if (donorExists) {
            donor = donorRepository.findByDonorId(id);
            donor.setAmount(donor.getAmount() + sum.doubleValue());
        } else {
            ApplicationUser user = userRepository.findById(id).orElse(null);
            if (user != null) {
                donor.setDonorId(id);
                donor.setAmount(sum.doubleValue());
                donor.setDonorName(user.getFirstname());
            }
        }
        donor.setHelpDone(true);
        donorRepository.save(donor);
        return true;
    }

    @Override
    public Donors getDonationById(Long id) {
        try {
            return donorRepository.findByDonorId(id);
        } catch (Exception ex) {
            return null;
        }

    }
}
