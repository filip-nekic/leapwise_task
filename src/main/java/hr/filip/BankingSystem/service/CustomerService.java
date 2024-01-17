package hr.filip.BankingSystem.service;

import hr.filip.BankingSystem.model.Customer;
import hr.filip.BankingSystem.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    CustomerRepo customerRepo;

    @Autowired
    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public Optional<Customer> getCustomer(Long id) { return customerRepo.findById(id); }
}
