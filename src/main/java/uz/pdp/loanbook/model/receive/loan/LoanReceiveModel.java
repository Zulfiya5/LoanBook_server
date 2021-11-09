package uz.pdp.loanbook.model.receive.loan;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.loanbook.entity.loan.enums.Currency;
import uz.pdp.loanbook.entity.loan.enums.LoanType;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoanReceiveModel {
    @NotNull
    private BigDecimal amount;

    @NotNull
    private String purpose;


    @NotNull
    private Currency currency;

    @NotNull
    @JsonProperty("loan_type")
    private String loanType;

    @NotNull
    @JsonProperty("due_date")
    private Date dueDate;


    private String details;

    @NotNull
    @JsonProperty("user_id")
    private int userId;


}
