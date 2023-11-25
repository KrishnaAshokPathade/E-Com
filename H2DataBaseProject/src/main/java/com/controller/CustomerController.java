package com.controller;

import com.model.Customer;
import com.repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
    @Autowired
    private CustomerRepo customerRepo;

    @PostMapping("/save")
    public String save(@RequestBody Customer customer) {
       try{ Customer save = this.customerRepo.save(customer);
        return "Customer Save";
    }
       catch(Exception e){
       return "Customer Not Save";
       }
    }

}
