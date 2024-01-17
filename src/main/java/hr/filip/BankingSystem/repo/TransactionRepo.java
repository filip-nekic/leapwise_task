package hr.filip.BankingSystem.repo;

import hr.filip.BankingSystem.model.Transaction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    List<Transaction> findAll(Specification<Transaction> spec);

    List<Transaction> findAllByTimestampAfter(Instant timestamp);
}
