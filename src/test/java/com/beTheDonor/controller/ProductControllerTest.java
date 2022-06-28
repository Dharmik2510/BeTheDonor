package com.beTheDonor.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.beTheDonor.entity.Product;
import com.beTheDonor.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
// To start the application context and to detect the start up issues
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebMvcTest(controllers = ProductController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(locations = "/test-context.xml")
public class ProductControllerTest {
	// creating a mock object of the class ProductRepository
	@Mock
	private ProductService productService;

	private ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private MockMvc mockMvc;
	// calling junit - injecting the mock object in ProductController class
	@MockBean
	ProductController controller;
// This method to be executed before each invocation of testUpdateProducts
//	setup function will execute before the execution of every test case
    @BeforeEach
    public void setup() {
//    	Receiving the controller objects as parameters, building MOCKMvc instance by registering the controller instance
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

//    here we are making call to the api /api/v1/products put call and checking if we are getting success response (200 OK) or not
//for that we are mocking the service layer function productService.addQuantity
//    Creating mock product with the name "Dummy", with price 200 and with quantity 5
	@Test
	public void testUpdateProducts() throws Exception {
		Product product = new Product();
		product.setProductName("Dummy");
		product.setPrice(200.0d);
		product.setQuantity(5);
//		Checking with dummy data, passing any instance of product, if exists, then returning the product
		when(productService.addQuantity(Mockito.any(Product.class))).thenReturn(product);
//      Creating request builder to make request
//		mapper converts JSON object to JSON String
//		Calling API
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/v1/products").content(mapper.writeValueAsString(product))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.parseMediaType("application/json"));
//  API value is got
		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}
//	here we are making call to the api /api/v1/products get call and checking if we are getting success response (200 OK) or not
//for that we are mocking the service layer function productService.getProducts
	//    Creating mock product with the name "Dummy", with price 200 and with quantity 5
	@Test
	public void testGetProducts() throws Exception {
		List<Product> listProduct = new ArrayList<Product>();
		Product product = new Product();
		product.setProductName("Dummy");
		product.setPrice(200.0d);
		listProduct.add(product);
//		Checking with dummy data, if able to get the products, then returning the list of products
//		when(productService.getProducts()).thenReturn(listProduct);
		doReturn(listProduct).when(productService).getProducts();
//      Creating request builder to make request
//		mapper converts JSON object to JSON String
//		Calling API
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/products")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.parseMediaType("application/json"));

//  API value is got
		mockMvc.perform(requestBuilder).andExpect(status().isOk());

	}

}
