package com.beTheDonor.service.impl;

import com.beTheDonor.entity.ApplicationUser;
import com.beTheDonor.entity.UserConfirmationToken;
import com.beTheDonor.exception.ResourceNotFoundException;
import com.beTheDonor.exception.StockNotPresentException;
import com.beTheDonor.repository.ProductRepository;
import com.beTheDonor.entity.Product;
import com.beTheDonor.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product addQuantity(Product product) {
//		findById method retrieves the product by the Id
//		Return type is "Optional" return type
//		Optional- Container object which may or may not contain a non-null value.
//		If a value is present, isPresent returns true and returns the value.
        Optional<Product> optProduct = productRepository.findById(product.getProductId());
        Product dbProduct = null;
        if(optProduct.isPresent()) {
            dbProduct = optProduct.get();
//			Getting the product quantity from the database and adding this quantity to the new quantity specified by the PUT request
            dbProduct.setQuantity(dbProduct.getQuantity() + product.getQuantity());
//			Saving it in DB
            return productRepository.save(dbProduct);
        }else {
//			In case the Id is not found, then ResourceNotFoundException is thrown
            throw new ResourceNotFoundException("Product " + product.getProductName() +" not present");
        }

    }

    @Override
    public void removeQuantity(Product product) {
//		findById method retrieves the product by the Id
//		Return type is "Optional" return type
//		Optional- Container object which may or may not contain a non-null value.
//		If a value is present, isPresent returns true and get returns the value.
        Optional<Product> optProduct = productRepository.findById(product.getProductId());
        Product dbProduct = null;
        if (optProduct.isPresent()) {
            dbProduct = optProduct.get();
            if (dbProduct.getQuantity() > product.getQuantity()) {
//				Getting the product quantity from the database and subtracting this quantity to the new quantity specified by the DELETE request
                dbProduct.setQuantity(dbProduct.getQuantity() - product.getQuantity());
//				Saving it in DB
                productRepository.save(dbProduct);
            } else
                throw new StockNotPresentException("Stock not present :" + product.getQuantity());

        }else {
//			In case the Id is not found, then ResourceNotFoundException is thrown
            throw new ResourceNotFoundException("Product " + product.getProductName() + " not present");
        }
    }

    @Override
    public List<Product> getProducts() {
//		Returns all the products from the DB
        return productRepository.findAll();
    }

    @Override
    public Product addProduct(Product product){
        productRepository.save(product);
        return  product;
    }

    public void addProductinTable(Product product)
    {
        productRepository.save(product);
    }
    public void deleteProductinTable(long id)
    {
        productRepository.deleteById(id);
    }

    public void updateProductinTable(int qty, double price, long id)
    {
        productRepository.updateProducts(qty, price, id);
    }

    @Override
    public List<String> getCategories() {
        List<Product> product = productRepository.findAll();
        List<String> categories = new ArrayList<>();
        for(int i =0; i< product.size();i++) {
            if(!categories.contains(product.get(i).getCategory())) {
                categories.add(product.get(i).getCategory());
            }
        }
        return categories;
    }
}