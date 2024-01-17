package hr.filip.BankingSystem.service;

import hr.filip.BankingSystem.model.Account;
import hr.filip.BankingSystem.model.Transaction;
import hr.filip.BankingSystem.repo.AccountRepo;
import hr.filip.BankingSystem.repo.TransactionRepo;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TransactionService {

    TransactionRepo transactionRepo;
    AccountRepo accountRepo;
    EmailService emailService;

    @Autowired
    public TransactionService(TransactionRepo transactionRepo, AccountRepo accountRepo, EmailService emailService) {
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
        this.emailService = emailService;
    }

    @Transactional
    public Transaction addTransaction(Transaction transaction) {
        Optional<Account> senderAccount = accountRepo.findById(transaction.getSenderAccountId());
        Optional<Account> receiverAccount = accountRepo.findById(transaction.getReceiverAccountId());

        boolean isValidTransaction = isValid(transaction, senderAccount, receiverAccount);
        Transaction savedTransaction = isValidTransaction
                ? transactionRepo.save(transaction)
                : transaction;

        processTransaction(savedTransaction, senderAccount, receiverAccount, isValidTransaction);

        return savedTransaction;
    }
    @Transactional
    private void processTransaction(Transaction transaction, Optional<Account> senderAccount,
                                               Optional<Account> receiverAccount, boolean status) {
        if (status) {
            emailService.sendConfirmationEmail(transaction, senderAccount.get(), true);
            emailService.sendConfirmationEmail(transaction, receiverAccount.get(), true);
            updateBalances(senderAccount.get(), receiverAccount.get(), transaction.getAmount());
        } else {
            senderAccount.ifPresent(account -> emailService.sendConfirmationEmail(transaction, account, false));
            receiverAccount.ifPresent(account -> emailService.sendConfirmationEmail(transaction, account, false));
            throw new IllegalArgumentException("Transaction is invalid.");
        }
    }
    @Transactional
    private void updateBalances(Account senderAccount, Account receiverAccount, double amount) {
        double senderNewBalance = senderAccount.getBalance() - amount;
        double receiverNewBalance = receiverAccount.getBalance() + amount;

        senderAccount.setBalance(senderNewBalance);
        receiverAccount.setBalance(receiverNewBalance);
        accountRepo.save(senderAccount);
        accountRepo.save(receiverAccount);
    }

    private boolean isValid(Transaction transaction, Optional<Account> senderAccount,
                              Optional<Account> receiverAccount) {
        return !(transaction.getAmount() < 0 ||
                transaction.getMessage().equals("") ||
                senderAccount.isEmpty() ||
                receiverAccount.isEmpty());
    }
    public static Specification<Transaction> customFilter(List<Long> accountIds, Map<String, String> params) {
        return (root, query, builder) -> {
            Predicate senderPredicate = root.get("senderAccountId").in(accountIds);
            Predicate receiverPredicate = root.get("receiverAccountId").in(accountIds);
            Predicate customerPredicate = builder.or(senderPredicate, receiverPredicate);

            List<Predicate> filterPredicates = new ArrayList<>();
            if(!params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    filterPredicates.add(builder.equal(root.get(entry.getKey()), entry.getValue()));
                }
            }
            if (filterPredicates.size() > 0) {
                return builder.and(customerPredicate,builder.and(filterPredicates.toArray(new Predicate[0])));
            } else {
                return customerPredicate;
            }
        };
    }

    public List<Transaction> getAllByFilter(Specification<Transaction> customFilter) {
        return transactionRepo.findAll(customFilter);
    }

}
