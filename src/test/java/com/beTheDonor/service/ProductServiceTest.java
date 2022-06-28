package com.beTheDonor.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.beTheDonor.entity.Product;
import com.beTheDonor.repository.ProductRepository;
import com.beTheDonor.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

// To start the application context and to detect the start up issues
@SpringBootTest
public class ProductServiceTest {
// creating a mock object of the class ProductRepository
	@Mock
	private ProductRepository productRepository;
// calling junit
	@InjectMocks
ProductServiceImpl service;
	//    Creating mock product with the name "Dummy", with price 200 and with quantity 5
	@Test
	public void testGetProduct() throws Exception {
		List<Product> listproduct = new ArrayList<Product>();
		Product product = new Product();
		product.setProductName("Dummy");
		product.setPrice(200.0d);
		product.setQuantity(5);
		listproduct.add(product);
//		Checking with dummy data,if it exists, then returning the list of products
		when(productRepository.findAll()).thenReturn(listproduct);
//      Checking with dummy data and checking to see if it not null and has at least 1 product
		List<Product> productResp = service.getProducts();
		assertThat(productResp).isNotNull();
		assertThat(productResp).hasSize(1);

	}
	//    Creating mock product with the name "Dummy", with price 200 and with quantity 5
	@Test
	public void testUpdateProduct() throws Exception {
		Product product = new Product();
		product.setProductName("Dummy");
		product.setPrice(200.0d);
		product.setQuantity(5);
//		Checking with dummy data, not saving it in db, but returning the dummy product
		when(productRepository.save(Mockito.any())).thenReturn(product);
		when(productRepository.findById(Mockito.any())).thenReturn(Optional.of(product));
//      Checking with dummy data for updating and checking to see if it not null and has at least 1 product
		Product productResp = service.addQuantity(product);
		assertThat(productResp).isNotNull();

	}

}
