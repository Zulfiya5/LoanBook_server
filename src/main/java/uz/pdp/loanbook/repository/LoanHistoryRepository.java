package uz.pdp.loanbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.loanbook.entity.loan.LoanHistoryDatabase;

import java.util.List;

public interface LoanHistoryRepository extends JpaRepository<LoanHistoryDatabase, Integer> {

}
