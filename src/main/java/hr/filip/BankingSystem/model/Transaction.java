package hr.filip.BankingSystem.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name="Transactions")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long transactionId;
    @Getter
    @Setter
    private Long senderAccountId;
    @Getter
    @Setter
    private Long receiverAccountId;
    @Getter
    @Setter
    private Double amount;
    @Getter
    @Setter
    private Long currencyId;
    @Getter
    @Setter
    private String message;
    @Getter
    @Setter
    private Instant timestamp;


}
