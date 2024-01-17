package hr.filip.BankingSystem.service;

import hr.filip.BankingSystem.model.Account;
import hr.filip.BankingSystem.model.Transaction;
import hr.filip.BankingSystem.repo.AccountRepo;
import hr.filip.BankingSystem.repo.TransactionRepo;
import hr.filip.BankingSystem.utility.Helper;
import hr.filip.BankingSystem.utility.IncomeAndExpenditure;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Service
public class AccountService {

    private AccountRepo accountRepo;
    private TransactionRepo transactionRepo;

    public AccountService(AccountRepo accountRepo, TransactionRepo transactionRepo) {

        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
    }

    public void calculateTurnoverOnStartup() {
        calculateAndUpdateTurnover(LocalDate.now().withDayOfMonth(1));
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void runMonthly() {
        calculateAndUpdateTurnover(LocalDate.now());
    }

    @Transactional
    public void calculateAndUpdateTurnover(LocalDate dateFrom) {
        List<Account> accounts = new ArrayList<>(accountRepo.findAll());
        Instant oneMonthAgoTimestamp = Helper.calculateTimeBack(dateFrom.minusMonths(1));
        List<Transaction> pastMonthTransactions = transactionRepo.findAllByTimestampAfter(oneMonthAgoTimestamp);
        Map<Long, IncomeAndExpenditure> incomesAndExpenditures = new HashMap<>();

        for (Transaction t : pastMonthTransactions) {
            long senderAccount = t.getSenderAccountId();
            long receiverAccount = t.getReceiverAccountId();
            double amount = t.getAmount();
            incomesAndExpenditures.compute(senderAccount, (key, oldValue) -> (oldValue == null) ? new IncomeAndExpenditure(0.0, amount)
                    : oldValue.setExpenditure(oldValue.getExpenditure() + amount));
            incomesAndExpenditures.compute(receiverAccount, (key, oldValue) -> (oldValue == null) ? new IncomeAndExpenditure(amount, 0.0)
                    : oldValue.setIncome(oldValue.getIncome() + amount));
        }

        for (Account account : accounts) {
            IncomeAndExpenditure temp = incomesAndExpenditures.get(account.getAccountId());
            double turnover = temp.getIncome() - temp.getExpenditure();
            account.setPastMonthTurnover(turnover);
            accountRepo.save(account);
        }
    }

}
