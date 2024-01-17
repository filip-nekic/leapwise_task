package hr.filip.BankingSystem.service;

import hr.filip.BankingSystem.model.Account;
import hr.filip.BankingSystem.model.Customer;
import hr.filip.BankingSystem.model.Transaction;
import hr.filip.BankingSystem.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private JavaMailSender mailSender;
    private CustomerRepo customerRepo;

    @Autowired
    public EmailService(JavaMailSender mailSender, CustomerRepo customerRepo) {
        this.mailSender = mailSender;
        this.customerRepo = customerRepo;
    }

    public void sendEmail(String toEmail,
                          String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("testing.app4322@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        mailSender.send(message);
    }
    public void sendConfirmationEmail(Transaction transaction, Account account, boolean status) {
        String emailBody = createEmailBody(transaction, account, status);
        Customer customer = customerRepo.findByAccountsIs(account);
        System.out.println("Email to: " + customer.getEmail() + "\nBody:\n" + emailBody);
        //sendEmail(customer.getEmail(), emailBody);
    }

    public String createEmailBody(Transaction transaction, Account account, boolean status) {
        String action = status ? (transaction.getSenderAccountId() == account.getAccountId() ? "taken from": "added to") : "taken from/added to";
        double balance = account == null ? 0.0: account.getBalance();
        double amount = status ? transaction.getAmount(): 0.0;
        double newBalance = balance;
        if(status) {
            newBalance = transaction.getSenderAccountId() == account.getAccountId() ?
                    (account.getBalance() - transaction.getAmount()) : (account.getBalance() + transaction.getAmount());
        }
        Long transactionId = status ?  transaction.getTransactionId(): -4444;
        String transactionStatus = status ? "successfully" : "unsuccessfully";
        String emailContent = "Hello!\n\n" +
                "The transaction with ID: " + transactionId +
                " has been processed " + transactionStatus + ",\n" +
                "and the balance: " + amount + " has been " + action + " your account.\n\n" +
                "Old balance: " + balance + "\n" +
                "New balance: " + newBalance + "\n\n" +
                "Regards,\nYour XYZ bank";
        return emailContent;
    }
}
