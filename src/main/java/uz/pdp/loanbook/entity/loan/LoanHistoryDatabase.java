package uz.pdp.loanbook.entity.loan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import uz.pdp.loanbook.entity.loan.enums.Currency;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LoanHistoryDatabase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private BigDecimal amount;
    private String purpose;
    private Currency currency;
    private Date dueDate;
    private String details;
    private String loanType;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

  /* public String getDueDate() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format((TemporalAccessor) dueDate);
    }*/
}
