package com.beTheDonor.controller;

import com.beTheDonor.repository.ProductRepository;
import com.beTheDonor.entity.Product;
import com.beTheDonor.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/products")
@AllArgsConstructor
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;

    @GetMapping()
    public ResponseEntity<List<Product>> getProducts() {
        return new ResponseEntity<List<Product>>(productService.getProducts(), HttpStatus.OK);
    }

    @PutMapping()
//	ResponseEntity class<T> which extends HttpClass<T>
    public ResponseEntity<Product> addQuantity(@RequestBody Product product) {
//		adds the HTTPStatus code
//		Calling the addQuantity method of the interface ProductService
        return new ResponseEntity<Product>(productService.addQuantity(product), HttpStatus.OK);
    }

    @DeleteMapping()
//	ResponseEntity class<T> which extends HttpClass<T>
    public ResponseEntity<Void> deleteQuantity(@RequestBody Product product) {
//		adds the HTTPStatus code
//		Calling the removeQuantity method of the interface ProductService
        productService.removeQuantity(product);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping()
//	ResponseEntity class<T> which extends HttpClass<T>
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
//		adds the HTTPStatus code
//		Calling the addQuantity method of the interface ProductService
        return new ResponseEntity<Product>(productService.addProduct(product), HttpStatus.OK);
    }

    @ModelAttribute
    public String addAttributes(Model model) {
        List<Product> product = productRepository.findAll();
        model.addAttribute("products", product);
        return "products";
    }

    @GetMapping("/getCategories")
    public ResponseEntity<List<String>> getCategories() {
        return new ResponseEntity<List<String>>(productService.getCategories(), HttpStatus.OK);
    }
}
