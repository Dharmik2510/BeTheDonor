package com.beTheDonor.service.impl;

import com.beTheDonor.entity.*;
import com.beTheDonor.exception.ResourceNotFoundException;
import com.beTheDonor.repository.*;
import com.beTheDonor.service.CreditAmountService;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class OrderServiceImplTest {

    @Mock
    OrderServiceImpl orderServiceMock;

    @Mock
    OrderItemsRepository orderItemsRepository;

    @Autowired
    OrderServiceImpl orderServiceAuto;

    @InjectMocks
    OrderServiceImpl orderService;

    @Mock
    UserRepository userRepository;

    @Mock
    DeliveryAddressRepository deliveryAddressRepository;

    @MockBean
    ApplicationUser applicationUser;

    @Mock
    OrderRepository orderRepository;

    @MockBean
    DeliveryAddress deliveryAddress;

    @MockBean
    Orders order;

    @Mock
    JSONObject jsonObject;

    @Mock
    PatientOrdersResponse patientOrdersResponse;

    @BeforeEach
    void setUp() {
        orderServiceAuto = Mockito.spy(orderServiceAuto);
    }

    @Mock
    ProductRepository productRepository;

    @Mock
    CreditAmountService creditAmount;

    @Test
    @Rollback(value = false)
        void getApplicationUserTrue() {
        ApplicationUser applicationUser1 = ApplicationUser.builder().email("manali.s0106@gmail.com").build();
        userRepository.save(applicationUser1);
        doReturn(true).when(userRepository).existsByEmail(applicationUser1.getEmail());
        doReturn(applicationUser1).when(userRepository).getByEmail(applicationUser1.getEmail());
        ApplicationUser applicationUser2 = orderService.getApplicationUser("manali.s0106@gmail.com");
        Assertions.assertNotNull(applicationUser2);
        Assertions.assertEquals(applicationUser2.getEmail(),applicationUser1.getEmail());
    }

    @Test
    @Rollback(value = false)
    void getApplicationUserFalse() {
        ApplicationUser applicationUser1 = ApplicationUser.builder().email("manali.s0106@gmail.com").build();
        userRepository.save(applicationUser1);
        doReturn(false).when(userRepository).existsByEmail(applicationUser1.getEmail());
        Throwable ex;
        ex = assertThrows(ResourceNotFoundException.class,
                () -> {
                    orderService.getApplicationUser("manali.s0106@gmail.com");
                });
        assertEquals("User with User ID: manali.s0106@gmail.com is not present.",ex.getMessage());
    }

    @Test
    void getAllOrders() {
        Orders order = Orders.builder().build();
        List<Orders> orders = new ArrayList<>();
        orders.add(order);
        Mockito.when(orderRepository.findAll()).thenReturn(orders);
        Assertions.assertEquals(orders, orderService.getAllOrders());
    }

    @Test
    void getOrderResponse() {
        Orders order = Orders.builder().build();
        List<Orders> orders = new ArrayList<>();
        orders.add(order);

        PatientOrdersResponse patientOrdersResponse = this.patientOrdersResponse.builder().build();
        List<PatientOrdersResponse> orderResponses = new ArrayList<>();
        orderResponses.add(patientOrdersResponse);

        Mockito.when(orderServiceMock.getAllOrders()).thenReturn(orders);
        Mockito.when(orderServiceMock.formOrderResponses(orderResponses, orders)).thenReturn(orderResponses);
        Assertions.assertNotNull(orderService.getOrderResponse());
    }

    @Test
    void getOrderStatusPendingPayment() {
        String orderStatus = "pending payment";
        Assertions.assertEquals("Order Placed", orderService.getOrderStatus(orderStatus));
    }

    @Test
    void getOrderStatusPendingDelivery() {
        String orderStatus = "pending delivery";
        Assertions.assertEquals("Order Paid, Pending Delivery", orderService.getOrderStatus(orderStatus));
    }

    @Test
    void getOrderStatusDelivered() {
        String orderStatus = "delivered";
        Assertions.assertEquals("Order Delivered", orderService.getOrderStatus(orderStatus));
    }

    @Test
    void setOrderItems() {
        List<String> productName = new ArrayList<>();
        List<Double> price = new ArrayList<>();
        List<Integer> quantity = new ArrayList<>();

        productName.add("Milk");
        productName.add("Mask");
        price.add(20.0);
        price.add(10.0);
        quantity.add(5);
        quantity.add(10);

        List<OrderItem> orderItems = new ArrayList<>();
        Product product1 = Product.builder().productId(1L).productName("Milk").price(20.0).quantity(5).build();
        Product product2 = Product.builder().productId(2L).productName("Mask").price(10.0).quantity(10).build();
        OrderItem orderItem1 = OrderItem.builder().productId(product1).build();
        OrderItem orderItem2 = OrderItem.builder().productId(product2).build();
        orderItems.add(orderItem1);
        orderItems.add(orderItem2);
        PatientOrdersResponse patientOrdersResponse2 = new PatientOrdersResponse();
        Assertions.assertEquals(2, orderService.setOrderItems(patientOrdersResponse2,orderItems).getProductName().size());
    }

    @Test
    void saveDeliveryAddress() {
        DeliveryAddress deliveryAddress = new DeliveryAddress();
        ArrayList<HashMap<String, String>> jsonAddress = new ArrayList<>();
        HashMap<String,String> map = new HashMap<>();
        map.put("address","1991 Brunswick Street");
        map.put("city","Halifax");
        map.put("province","Nova Scotia");
        map.put("country","Canada");
        map.put("postalCode","B3J2G9");
        jsonAddress.add(map);
        Mockito.doReturn(deliveryAddress).when(deliveryAddressRepository).save(deliveryAddress);
        Assertions.assertEquals(deliveryAddress, orderService.saveDeliveryAddress(deliveryAddress, jsonAddress));
    }


    @Test
    @Rollback(value = false)
    void saveOrderItemsTrue() {
        Product product = Product.builder().productId(1L).quantity(5).build();
        OrderItem orderItem = OrderItem.builder().id(1L).productId(product).build();
        productRepository.save(product);
        ArrayList<HashMap<String, String>> orderItemsList = new ArrayList<>();
        HashMap<String,String> map = new HashMap<>();
        map.put("productId","1");
        map.put("quantity","2");
        orderItemsList.add(map);

        Mockito.when(productRepository.existsById(1L)).thenReturn(true);
        Mockito.when(productRepository.getById(1L)).thenReturn(product);

        Mockito.doReturn(orderItem).when(orderItemsRepository).save(orderItem);
        Mockito.doReturn(product).when(productRepository).save(product);
        Mockito.doNothing().when(orderServiceMock).updateProductQuantity(1L,3);
        orderService.saveOrderItems(order,orderItemsList);
        Assertions.assertEquals(orderItem.getProductId().getProductId(),Long.parseLong(orderItemsList.get(0).get("productId")));
    }

    @Test
    @Rollback(value = false)
    void saveOrderItemsFalse() {
        Product product = Product.builder().productId(1L).quantity(5).build();
        productRepository.save(product);
        ArrayList<HashMap<String, String>> orderItemsList = new ArrayList<>();
        HashMap<String,String> map = new HashMap<>();
        map.put("productId","1");
        map.put("quantity","2");
        orderItemsList.add(map);

        Mockito.when(productRepository.existsById(1L)).thenReturn(false);
        Throwable ex;
        ex = assertThrows(ResourceNotFoundException.class,
                () -> {
                    orderService.saveOrderItems(order,orderItemsList);
                });
        assertEquals("Product with Product ID: 1 is not present.",ex.getMessage());
    }

    @Test
    void saveNewOrder() {
        DeliveryAddress deliveryAddress = DeliveryAddress.builder().addressId(1L).build();
        deliveryAddressRepository.save(deliveryAddress);

        Mockito.doNothing().when(order).setUserId(applicationUser);
        Mockito.doReturn("10.0").when(jsonObject).get("total");

        Mockito.doNothing().when(order).setDeliveryAddressId(deliveryAddress);
        Mockito.doReturn(order).when(orderRepository).save(order);
        Mockito.doReturn(1L).when(order).getOrderId();
        Assertions.assertEquals(1L, orderService.saveNewOrder(order, jsonObject,applicationUser,deliveryAddress));
    }

    @Test
    void getOrderItems() {
        List<OrderItem> orderItems = new ArrayList<>();
        Mockito.doReturn(orderItems).when(orderItemsRepository).findByOrderId(order);
        Assertions.assertEquals(orderItems, orderService.getOrderItems(order));
    }

    @Test
    void getOrderItemsSize() {
        Orders orders = Orders.builder().orderId(1L).build();
        orderRepository.save(orders);

        OrderItem orderItem1 = OrderItem.builder().orderId(orders).id(1L).build();
        OrderItem orderItem2 = OrderItem.builder().orderId(orders).id(2L).build();
        orderItemsRepository.save(orderItem1);
        orderItemsRepository.save(orderItem2);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem1);
        orderItems.add(orderItem2);

        Mockito.doReturn(orderItems).when(orderItemsRepository).findByOrderId(orders);
        Assertions.assertEquals(2, orderService.getOrderItems(orders).size());
    }

    /*@Test
    void getOrdersByUserId() {

        List<Orders> orders = new ArrayList<>();

        Mockito.doReturn(applicationUser).when(orderServiceMock).getApplicationUser("test@gmail.com");
        Mockito.doReturn(orders).when(orderRepository).findByUserId(applicationUser);

        Assertions.assertEquals(orders,orderService.getOrdersByUserId("test@gmail.com"));

    }*/

   // @Test
    /*void addOrder() {
        Mockito.doReturn(applicationUser).when(orderServiceAuto).getApplicationUser("test@gmail.com");
        HashMap<String,String> map = new HashMap<>();
        map.put("address","1991 Brunswick Street");
        map.put("city","Halifax");
        map.put("province","Nova Scotia");
        map.put("country","Canada");
        map.put("postalCode","B3J2G9");
        ArrayList<HashMap<String,String>> jsonAddress = new ArrayList<>();
        jsonAddress.add(map);
        Mockito.doReturn(jsonAddress).when(jsonObject).get("address");
        Mockito.doReturn(deliveryAddress).when(deliveryAddressRepository).save(deliveryAddress);
        Mockito.when(orderServiceAuto.saveDeliveryAddress(deliveryAddress,jsonAddress)).thenReturn(deliveryAddress);
        Mockito.when(orderServiceAuto.saveNewOrder(order,jsonObject,applicationUser,deliveryAddress)).thenReturn(1L);
        Mockito.doReturn(order).when(orderRepository).getById(1L);

        ArrayList<HashMap<String,String>> orderItemsList = new ArrayList<>();
        HashMap<String,String> orderItemMap = new HashMap<>();
        orderItemMap.put("id","1");
        orderItemMap.put("productName","Apple");
        orderItemsList.add(orderItemMap);

        Mockito.doReturn(orderItemsList).when(jsonObject).get("order");
        Mockito.doNothing().when(orderServiceAuto).saveOrderItems(order,orderItemsList);

        Mockito.doNothing().when(creditAmount).orderFromCredits();

        Assertions.assertEquals(true, orderServiceAuto.addOrder(jsonObject,"test@gmail.com"));

    }*/
}