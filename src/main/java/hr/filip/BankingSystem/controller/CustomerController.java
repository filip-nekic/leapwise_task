package hr.filip.BankingSystem.controller;

import hr.filip.BankingSystem.model.Account;
import hr.filip.BankingSystem.model.Customer;
import hr.filip.BankingSystem.model.Transaction;
import hr.filip.BankingSystem.service.CustomerService;
import hr.filip.BankingSystem.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private CustomerService customerService;
    private TransactionService transactionService;

    @Autowired
    public CustomerController(CustomerService customerService, TransactionService transactionService) {
        this.customerService = customerService;
        this.transactionService = transactionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Optional<Customer> customer = customerService.getCustomer(id);
        if(customer.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customer.get(), HttpStatus.OK);
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<List<Transaction>> getTransactionHistory(@PathVariable Long id,
                                                             @RequestParam(required = false) Map<String, String> params) {
        Optional<Customer> customer = customerService.getCustomer(id);
        if(customer.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Long> accountIds = customer.get().getAccounts().stream().map(Account::getAccountId).collect(Collectors.toList());
        List<Transaction> transactions = transactionService.getAllByFilter(TransactionService.customFilter(accountIds,params));
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
