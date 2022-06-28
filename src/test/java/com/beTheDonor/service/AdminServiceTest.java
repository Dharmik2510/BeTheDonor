package com.beTheDonor.service;

import com.beTheDonor.controller.requestbody.ProductRequest;
import com.beTheDonor.entity.ApplicationUser;
import com.beTheDonor.entity.Product;
import com.beTheDonor.repository.UserRepository;
import com.beTheDonor.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class AdminServiceTest{

    @Mock
    AdminService adminServiceMock;

    @Mock
    ApplicationUserService applicationUserService;

    @Mock
    ProductServiceImpl productServiceImpl;

    @MockBean
    ApplicationUser applicationUser;

    @Mock
    ProductRequest productRequest;

    @Autowired
    AdminService adminServiceAuto;

    @InjectMocks
    AdminService adminService;

    @BeforeEach
    void setUp() {
        adminServiceAuto = Mockito.spy(adminServiceAuto);
    }

    @Test
    void getUsers(){
        List<ApplicationUser> applicationUsers = new ArrayList<>();
        applicationUsers.add(applicationUser);
        Mockito.doReturn(applicationUsers).when(applicationUserService).findAll();
        Assertions.assertEquals(applicationUsers,adminService.getUsers());
    }

    @Test
    void getPatient(){
        List<ApplicationUser> applicationUsers = new ArrayList<>();
        applicationUsers.add(applicationUser);
        Mockito.doReturn(applicationUsers).when(applicationUserService).getPatients();
        Assertions.assertEquals(applicationUsers,adminService.getPatients());
    }

    @Test
    void getDonor(){
        List<ApplicationUser> applicationUsers = new ArrayList<>();
        applicationUsers.add(applicationUser);
        Mockito.doReturn(applicationUsers).when(applicationUserService).getDonor();
        Assertions.assertEquals(applicationUsers,adminService.getDonors());
    }

    @Test
    void getRider(){
        List<ApplicationUser> applicationUsers = new ArrayList<>();
        applicationUsers.add(applicationUser);
        Mockito.doReturn(applicationUsers).when(applicationUserService).getRider();
        Assertions.assertEquals(applicationUsers,adminService.getRiders());
    }

    @Test
    void delete(){
        long id = 1;
        adminService.delete(id);
        Mockito.doNothing().when(productServiceImpl).deleteProductinTable(id);
    }

    @Test
    void update(){
        int qty = 20;
        double price = 60.0;
        long id = 1;
        adminService.update(qty,price,id);
        Mockito.doNothing().when(productServiceImpl).updateProductinTable(qty,price,id);
    }

    @Test
    void add(){
        ProductRequest productRequest = new ProductRequest();
        Product product = new Product("demo",15,20.0,"cat","kg");
        adminService.add(productRequest);
        Mockito.doNothing().when(productServiceImpl).addProductinTable(product);
    }
}