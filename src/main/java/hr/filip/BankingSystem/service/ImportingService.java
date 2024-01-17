package hr.filip.BankingSystem.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import hr.filip.BankingSystem.model.Account;
import hr.filip.BankingSystem.model.Customer;
import hr.filip.BankingSystem.model.Transaction;
import hr.filip.BankingSystem.repo.AccountRepo;
import hr.filip.BankingSystem.repo.CustomerRepo;
import hr.filip.BankingSystem.repo.TransactionRepo;
import hr.filip.BankingSystem.utility.AccountUtility;
import hr.filip.BankingSystem.utility.CustomerUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

@Service
public class ImportingService {

    private TransactionRepo transactionRepo;
    private AccountRepo accountRepo;
    private CustomerRepo customerRepo;
    private AccountService accountService;

    private static final int threadPoolSize = 4;

    @Autowired
    public ImportingService(TransactionRepo transactionRepo, AccountRepo accountRepo, CustomerRepo customerRepo,
                            AccountService accountService) {
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
        this.customerRepo = customerRepo;
        this.accountService = accountService;
    }

    public void readCsv() throws IOException {
        List<Transaction> transactionList = new ArrayList<>();
        Resource resource = new ClassPathResource("csv/transactions.csv");
        InputStream inputStream = resource.getInputStream();
        CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
        try {
            List<String[]> record = new ArrayList<>(reader.readAll());
            int transactionsPerThread = record.size() / threadPoolSize;

            List<Callable<List<Transaction>>> tasks = new ArrayList<>();

            for (int i = 0; i < threadPoolSize; i++) {
                int startIndex = i * transactionsPerThread;
                int endIndex = (i == threadPoolSize - 1) ? record.size() : (i + 1) * transactionsPerThread;
                tasks.add(() -> processTransactions(record.subList(startIndex, endIndex)));
            }
            ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
            List<Future<List<Transaction>>> futures = executorService.invokeAll(tasks);
            executorService.shutdown();

            for (Future<List<Transaction>> future : futures) {
                transactionList.addAll(future.get());
            }
        } catch (CsvException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        createCustomersAndAccounts(transactionList);

    }
    private static List<Transaction> processTransactions(List<String[]> lines) {
        List<Transaction> transactions = new ArrayList<>();
        for (String[] record : lines) {
            if (record[0].equals("transactionId")) continue;
            transactions.add(new Transaction(
                    Long.valueOf(record[0]),
                    Long.valueOf(record[1]),
                    Long.valueOf(record[2]),
                    Double.valueOf(record[3]),
                    Long.valueOf(record[4]),
                    record[5],
                    Instant.parse(record[6])
            ));
        }
        return transactions;
    }
    private void createCustomersAndAccounts(List<Transaction> transactionList) {
        int numberOfCustomers = 100;
        List<Customer> customers = CustomerUtility.createCustomers(numberOfCustomers);
        List<Account> accounts = AccountUtility.createAccounts(transactionList, customers);

        transactionRepo.saveAll(transactionList);
        accountRepo.saveAll(accounts);
        customerRepo.saveAll(customers);
        accountService.calculateTurnoverOnStartup();
    }


}
