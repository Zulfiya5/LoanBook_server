package uz.pdp.loanbook.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.loanbook.entity.loan.LoanDatabase;
import uz.pdp.loanbook.entity.user.UserDatabase;
import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<LoanDatabase, Integer> {
    Optional<Page<LoanDatabase>> findAllByUserDatabaseAndLoanTypeOrderByDueDate(UserDatabase userDatabase, String loanType, Pageable pageable);


}
