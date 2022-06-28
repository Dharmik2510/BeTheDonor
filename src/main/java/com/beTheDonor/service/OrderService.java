package com.beTheDonor.service;

import com.beTheDonor.entity.*;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public interface OrderService {

    Boolean addOrder(JSONObject payload, String userId);
    List<OrderItem> getOrderItems(Orders orders);

    ApplicationUser getApplicationUser(String userId);

    void saveOrderItems(Orders order, ArrayList<HashMap<String, String>> orderItemsList);

    void updateProductQuantity(Long productId, int quantity);

    Long saveNewOrder(Orders newOrder, JSONObject jsonObj, ApplicationUser user, DeliveryAddress deliveryAddress);

    DeliveryAddress saveDeliveryAddress(DeliveryAddress deliveryAddress, ArrayList<HashMap<String, String>> jsonAddress);

    List<Orders> getOrdersByUserId(String userId);
    List<PatientOrdersResponse> getOrdersResponseByUserId(String userId);
    List<Orders> getAllOrders();
    List<PatientOrdersResponse> getOrderResponse();

    List<PatientOrdersResponse> formOrderResponses(List<PatientOrdersResponse> orderResponses, List<Orders> orders);

    String getOrderStatus(String orderStatus);

    PatientOrdersResponse setOrderItems(PatientOrdersResponse orderResponse, List<OrderItem> orderItem);
}