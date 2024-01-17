package hr.filip.BankingSystem.utility;

import hr.filip.BankingSystem.model.Account;
import hr.filip.BankingSystem.model.Customer;
import hr.filip.BankingSystem.model.Transaction;

import java.util.*;

public class AccountUtility {

    public static List<Account> createAccounts(List<Transaction> transactions, List<Customer> customers) {
        List<Account> accounts = new ArrayList<>();
        Set<Long> accountIds = new HashSet<>();
        for (Transaction t : transactions) {
            if(!accountIds.contains(t.getSenderAccountId())) {
                Account account = createAccount(t.getSenderAccountId());
                accounts.add(account);
                accountIds.add(t.getSenderAccountId());
            }
            if(!accountIds.contains(t.getReceiverAccountId())) {
                Account account = createAccount(t.getReceiverAccountId());
                accounts.add(account);
                accountIds.add(t.getReceiverAccountId());
            }
        }
        for(Account a: accounts) {
            Random r = new Random();
            customers.get(r.nextInt(customers.size())).getAccounts().add(a);
        }
        return accounts;
    }

    private static Account createAccount(Long accountId) {
        Random r= new Random();
        String accountNumber = "HR3898" + Helper.generateRandomLong(100000000, 999999999);
        List<String> accountTypes = new ArrayList<>(Arrays.asList("Checking", "Savings", "Money Market", "Certificate of deposit accounts"));
        double balance = Helper.generateRandomDouble(0.0, 1000.0);
        return new Account(accountId, accountNumber, accountTypes.get(r.nextInt(accountTypes.size())), balance, 0.0);
    }

}
