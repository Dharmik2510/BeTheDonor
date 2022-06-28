package com.beTheDonor.service;
import com.beTheDonor.controller.requestbody.ProductRequest;
import com.beTheDonor.controller.requestbody.RegistrationRequest;
import com.beTheDonor.entity.ApplicationUser;
import com.beTheDonor.entity.Product;
import com.beTheDonor.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
/*@AllArgsConstructor*/
public class AdminService {
    @Autowired
    ApplicationUserService ap;
    ProductRequest productRequest;
    @Autowired
    ProductServiceImpl psi;
    public List<ApplicationUser> getUsers() { return ap.findAll();}
    public List<ApplicationUser> getPatients(){ return ap.getPatients(); }
    public List<ApplicationUser> getRiders(){ return ap.getRider(); }
    public List<ApplicationUser> getDonors(){ return ap.getDonor(); }
    public void add(ProductRequest productRequest)
    {
        psi.addProductinTable(new Product(
                productRequest.getProductName(),
                productRequest.getQuantity(),
                productRequest.getPrice(),
                productRequest.getCategory(),
                productRequest.getComment()
        ));
    }
    public void delete(long id)
    {
        psi.deleteProductinTable(id);
    }

    public void update(int qty, double price, long id)
    {
        psi.updateProductinTable(qty, price, id);
    }
}
