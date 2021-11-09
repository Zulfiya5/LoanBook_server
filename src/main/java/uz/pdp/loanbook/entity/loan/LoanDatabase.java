package uz.pdp.loanbook.entity.loan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import uz.pdp.loanbook.entity.loan.enums.Currency;
import uz.pdp.loanbook.entity.loan.enums.LoanType;
import uz.pdp.loanbook.entity.user.UserDatabase;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LoanDatabase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private BigDecimal amount;
    private String purpose;
    private Currency currency;
    private Date dueDate;
    private String details;
    private String loanType;

    @ManyToOne
    private UserDatabase userDatabase;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LoanHistoryDatabase> loanHistoryDatabaseList;
}
