package uz.pdp.loanbook.service.loan;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.pdp.loanbook.entity.loan.LoanDatabase;
import uz.pdp.loanbook.entity.loan.LoanHistoryDatabase;
import uz.pdp.loanbook.entity.user.UserDatabase;
import uz.pdp.loanbook.model.receive.loan.LoanReceiveModel;
import uz.pdp.loanbook.model.response.BaseResponseModel;
import uz.pdp.loanbook.repository.LoanHistoryRepository;
import uz.pdp.loanbook.repository.LoanRepository;
import uz.pdp.loanbook.service.user.UserService;
import uz.pdp.loanbook.service.base.BaseService;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanService extends BaseService<LoanReceiveModel, BaseResponseModel> {

    private final LoanRepository loanRepository;
    private final LoanHistoryRepository loanHistoryRepository;
    private final UserService userService;

    @Override
    public BaseResponseModel add(LoanReceiveModel loanReceiveModel) {
        LoanDatabase loanDatabase = new LoanDatabase();
        return saveLoan(loanReceiveModel, loanDatabase);
    }

    @Override
    public BaseResponseModel edit(LoanReceiveModel loanReceiveModel, Integer id) {
        Optional<LoanDatabase> optionalLoanDatabase = loanRepository.findById(id);
        if (optionalLoanDatabase.isEmpty())
            return ERROR;
        List<LoanHistoryDatabase> loanHistoryDatabaseList = saveLoanHistory(loanReceiveModel, id);
        LoanDatabase loanDatabase = optionalLoanDatabase.get();
        return updateLoan(loanReceiveModel, loanDatabase, loanHistoryDatabaseList);
    }

    @Override
    public BaseResponseModel delete(Integer id) {
        try {
            loanRepository.deleteById(id);
            return SUCCESS;
        } catch (Exception e) {
            return ERROR;
        }
    }

    public BaseResponseModel list(Integer userId, String loanType, Pageable pageable) {
        Optional<Page<LoanDatabase>> optionalLoanDatabaseList =
                loanRepository.findAllByUserDatabaseAndLoanTypeOrderByDueDate(getUserById(userId), loanType, pageable);
        if (optionalLoanDatabaseList.isEmpty())
            return ERROR;
        SUCCESS.setObject(optionalLoanDatabaseList.get());
        return SUCCESS;
    }

    private BaseResponseModel saveLoan(
            LoanReceiveModel loanReceiveModel,
            LoanDatabase loanDatabase
    ) {
        loanDatabase.setAmount(loanReceiveModel.getAmount());
        loanDatabase.setPurpose(loanReceiveModel.getPurpose());
        loanDatabase.setCurrency(loanReceiveModel.getCurrency());
        loanDatabase.setDueDate(loanReceiveModel.getDueDate());
        loanDatabase.setLoanType(loanReceiveModel.getLoanType());
        loanDatabase.setDetails(loanReceiveModel.getDetails());
        loanDatabase.setUserDatabase(getUserById(loanReceiveModel.getUserId()));
        loanRepository.save(loanDatabase);
        return SUCCESS;
    }

    private BaseResponseModel updateLoan(
            LoanReceiveModel loanReceiveModel,
            LoanDatabase loanDatabase,
            List<LoanHistoryDatabase> loanHistoryDatabaseList
    ) {
        BigDecimal newAmount = loanDatabase.getAmount().add(loanReceiveModel.getAmount());
        loanDatabase.setAmount(newAmount);
        loanDatabase.setPurpose(loanReceiveModel.getPurpose());
        loanDatabase.setCurrency(loanReceiveModel.getCurrency());
        loanDatabase.setDueDate(loanReceiveModel.getDueDate());
        loanDatabase.setLoanType(loanReceiveModel.getLoanType());
        loanDatabase.setDetails(loanReceiveModel.getDetails());
        loanDatabase.setUserDatabase(getUserById(loanReceiveModel.getUserId()));
        loanDatabase.setLoanHistoryDatabaseList(loanHistoryDatabaseList);
        loanRepository.save(loanDatabase);
        return SUCCESS;
    }

    private List<LoanHistoryDatabase> saveLoanHistory(
            LoanReceiveModel loanReceiveModel,
            Integer id
    ) {
        LoanHistoryDatabase loanHistoryDatabase = new LoanHistoryDatabase();
        loanHistoryDatabase.setAmount(loanReceiveModel.getAmount());
        loanHistoryDatabase.setPurpose(loanReceiveModel.getPurpose());
        loanHistoryDatabase.setCurrency(loanReceiveModel.getCurrency());
        loanHistoryDatabase.setDueDate(loanReceiveModel.getDueDate());
        loanHistoryDatabase.setLoanType(loanReceiveModel.getLoanType());
        loanHistoryDatabase.setDetails(loanReceiveModel.getDetails());
        LoanHistoryDatabase savedLoanHistory = loanHistoryRepository.save(loanHistoryDatabase);
        List<LoanHistoryDatabase> loanHistoryDatabaseList =
                loanRepository
                        .findById(id)
                        .get()
                        .getLoanHistoryDatabaseList();
        loanHistoryDatabaseList.add(savedLoanHistory);
        return loanHistoryDatabaseList
                .stream()
                .sorted(Comparator.comparing(LoanHistoryDatabase::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    private UserDatabase getUserById(Integer id) {
        return userService.get(id);
    }
}
