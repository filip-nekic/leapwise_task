package hr.filip.BankingSystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name="Accounts")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Account {
    @Id
    @Getter
    @Setter
    private Long accountId;
    @Getter
    @Setter
    private String accountNumber;
    @Getter
    @Setter
    private String accountType;
    @Getter
    @Setter
    private Double balance;
    @Getter
    @Setter
    private double pastMonthTurnover;
}
