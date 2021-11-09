package uz.pdp.loanbook.controller.loan;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.loanbook.model.receive.loan.LoanReceiveModel;
import uz.pdp.loanbook.model.response.BaseResponseModel;
import uz.pdp.loanbook.service.loan.LoanService;

import javax.validation.Valid;


@RestController
@RequestMapping("api/loan")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/add")
    public HttpEntity<?> addLoan(
            @Valid @RequestBody LoanReceiveModel takenLoanReceiveModel
    ) {
        BaseResponseModel response = loanService.add(takenLoanReceiveModel);
        return ResponseEntity.status(response.getSuccess() ?
                HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
    }

    @GetMapping("/list/{id}/{loanType}")
    public HttpEntity<?> getLoansByUserIdAndLoanType(
            @PathVariable("id") Integer id,
            @PathVariable("loanType") String loanType,
            Pageable pageable
    ) {
        return ResponseEntity.ok(loanService.list(id, loanType, pageable));
    }


    @PutMapping("/edit/{id}")
    public HttpEntity<?> editLoan(
            @PathVariable Integer id,
            @Valid @RequestBody LoanReceiveModel loanReceiveModel
    ) {
        BaseResponseModel response = loanService.edit(loanReceiveModel, id);
        return ResponseEntity.status(response.getSuccess() ?
                HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(response);
    }


    @DeleteMapping("/delete/{id}")
    public HttpEntity<?> deleteLoan(
            @PathVariable Integer id
    ) {
        BaseResponseModel response = loanService.delete(id);
        return ResponseEntity.status(response.getSuccess() ?
                HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(response);

    }


}
