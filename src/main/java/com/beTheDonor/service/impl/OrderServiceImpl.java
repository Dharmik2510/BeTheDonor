package com.beTheDonor.service.impl;

import com.beTheDonor.entity.*;
import com.beTheDonor.exception.ResourceNotFoundException;
import com.beTheDonor.repository.*;
import com.beTheDonor.service.CreditAmountService;
import com.beTheDonor.service.OrderService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemsRepository orderItemsRepository;

    @Autowired
    DeliveryAddressRepository deliveryAddressRepository;

    @Autowired
    CreditAmountService creditAmountService;

    @Override
    public Boolean addOrder(JSONObject payload, String userId) {

        ApplicationUser user = getApplicationUser(userId);
        System.out.println(user.getEmail());
        //get address json from payload and save to db
        ArrayList<HashMap<String,String>> jsonAddress = (ArrayList) payload.get("address");
        DeliveryAddress deliveryAddress = new DeliveryAddress();
        deliveryAddress = saveDeliveryAddress(deliveryAddress,jsonAddress);

        //save new order
        Orders newOrder = new Orders();
        Long orderId = saveNewOrder(newOrder, payload, user, deliveryAddress);

        Orders order = orderRepository.getById(orderId);

        //get order items of the order from payload and save to db
        ArrayList<HashMap<String,String>> orderItemsList = (ArrayList) payload.get("order");
        saveOrderItems(order, orderItemsList);

        //call auto payment method
        creditAmountService.orderFromCredits();

        return true;
    }

    @Override
    public ApplicationUser getApplicationUser(String userId) {
        ApplicationUser user;
        boolean userExists = userRepository.existsByEmail(userId);
        if (userExists) {
            user = userRepository.getByEmail(userId);
        } else {
            throw new ResourceNotFoundException("User with User ID: "+ userId +" is not present.");
        }
        return user;
    }

    @Override
    public void saveOrderItems(Orders order, ArrayList<HashMap<String, String>> orderItemsList) {
        for (HashMap<String,String> map:
                orderItemsList) {
            OrderItem newOrderItems = new OrderItem();
            newOrderItems.setOrderId(order);
            Long productId = Long.parseLong(map.get("productId"));
            Product product;
            boolean productExists = productRepository.existsById(productId);
            if(productExists) {
                product = productRepository.getById(productId);
            } else {
                throw new ResourceNotFoundException("Product with Product ID: "+productId +" is not present.");
            }
            newOrderItems.setProductId(product);

            int quantity = Integer.parseInt(map.get("quantity"));
            newOrderItems.setQuantity(quantity);

            orderItemsRepository.save(newOrderItems);
            System.out.println(orderItemsRepository.count());
            updateProductQuantity(productId, quantity);
        }
    }

    @Override
    public void updateProductQuantity(Long productId, int quantity) {
        Product updateProduct = productRepository.getById(productId);
        updateProduct.setQuantity(updateProduct.getQuantity() - quantity);
        productRepository.save(updateProduct);
    }

    @Override
    public Long saveNewOrder(Orders newOrder, JSONObject jsonObj, ApplicationUser user, DeliveryAddress deliveryAddress) {

        newOrder.setUserId(user);
        Double total = Double.parseDouble((String) jsonObj.get("total"));
        newOrder.setTotal(total);
        newOrder.setOrderStatus("pending payment");
        newOrder.setDeliveryAddressId(deliveryAddressRepository.getById(deliveryAddress.getAddressId()));
        orderRepository.save(newOrder);
        Long orderId = newOrder.getOrderId();
        return orderId;
    }

    public DeliveryAddress saveDeliveryAddress(DeliveryAddress deliveryAddress, ArrayList<HashMap<String, String>> jsonAddress) {

        for (HashMap<String,String> map:
                jsonAddress) {
            String city = map.get("city");
            deliveryAddress.setCity(city);
            String address = map.get("address");
            deliveryAddress.setAddress(address);
            String country = map.get("country");
            deliveryAddress.setCountry(country);
            String postalCode = map.get("postalCode");
            deliveryAddress.setPostalCode(postalCode);
            String province = map.get("province");
            deliveryAddress.setProvince(province);
            deliveryAddressRepository.save(deliveryAddress);
        }
        return deliveryAddress;
    }

    @Override
    public List<Orders> getOrdersByUserId(String userId) {
        ApplicationUser user = getApplicationUser(userId);
        List<Orders> orders = orderRepository.findByUserId(user);
        return orders;
    }

    @Override
    public List<OrderItem> getOrderItems(Orders orders) {
        List<OrderItem> orderItems;
        orderItems = orderItemsRepository.findByOrderId(orders);
        return orderItems;
    }

    @Override
    public List<PatientOrdersResponse> getOrdersResponseByUserId(String userId) {
        List<PatientOrdersResponse> orderResponses = new ArrayList<>();
        List<Orders> orders = getOrdersByUserId(userId);
        return formOrderResponses(orderResponses, orders);
    }

    @Override
    public List<Orders> getAllOrders() {
        List<Orders> orders = orderRepository.findAll();
        return orders;
    }

    @Override
    public List<PatientOrdersResponse> getOrderResponse() {
        List<PatientOrdersResponse> orderResponses = new ArrayList<>();
        List<Orders> orders = getAllOrders();
        return formOrderResponses(orderResponses, orders);
    }

    @Override
    public List<PatientOrdersResponse> formOrderResponses(List<PatientOrdersResponse> orderResponses, List<Orders> orders) {
        for (int i = 0; i < orders.size(); i++) {
            PatientOrdersResponse orderResponse = new PatientOrdersResponse();
            orderResponse.setOrderId(orders.get(i).getOrderId());
            orderResponse.setTotalAmount(orders.get(i).getTotal());
            orderResponse.setFirstName(orders.get(i).getUserId().getFirstname());
            orderResponse.setLastName(orders.get(i).getUserId().getLastname());
            orderResponse.setOrderStatus(getOrderStatus(orders.get(i).getOrderStatus()));

            List<OrderItem> orderItem = getOrderItems(orders.get(i));
            orderResponse = setOrderItems(orderResponse, orderItem);
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }

    @Override
    public String getOrderStatus(String orderStatus) {
        if(orderStatus.equals("pending payment")) {
            orderStatus = "Order Placed";
        } else if(orderStatus.equals("pending delivery")) {
            orderStatus = "Order Paid, Pending Delivery";
        } else if(orderStatus.equals("delivered")) {
            orderStatus = "Order Delivered";
        }
        return orderStatus;
    }

    @Override
    public PatientOrdersResponse setOrderItems(PatientOrdersResponse orderResponse, List<OrderItem> orderItem) {
        List<String> productName = new ArrayList<>();
        List<Double> price = new ArrayList<>();
        List<Integer> quantity = new ArrayList<>();
        for (int j = 0; j < orderItem.size(); j++) {
            productName.add(orderItem.get(j).getProductId().getProductName());
            price.add(orderItem.get(j).getProductId().getPrice());
            quantity.add(orderItem.get(j).getQuantity());
        }
        orderResponse.setProductName(productName);
        orderResponse.setQuantity(quantity);
        orderResponse.setPrice(price);
        return orderResponse;
    }

}