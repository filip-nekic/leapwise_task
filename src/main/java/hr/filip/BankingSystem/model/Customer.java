package hr.filip.BankingSystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Table(name="Customers")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Customer {
    @Id
    @Getter
    @Setter
    private Long customerId;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String address;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String phoneNumber;
    @Getter
    @Setter
    @OneToMany
    private List<Account> accounts;
}
