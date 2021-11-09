package uz.pdp.loanbook.service.user;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.loanbook.entity.role.RoleName;
import uz.pdp.loanbook.entity.user.UserDatabase;
import uz.pdp.loanbook.model.receive.user.UserSignUpReceiveModel;
import uz.pdp.loanbook.model.response.BaseResponseModel;
import uz.pdp.loanbook.repository.RoleRepository;
import uz.pdp.loanbook.repository.UserRepository;
import uz.pdp.loanbook.service.base.BaseService;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService extends BaseService<UserSignUpReceiveModel, BaseResponseModel> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public BaseResponseModel add(UserSignUpReceiveModel userSignUpReceiveModel) {
        boolean existsByUsername = userRepository
                .existsByUsername(userSignUpReceiveModel.getUsername());
        if (existsByUsername)
            return ERROR_USERNAME;
        UserDatabase userDatabase = new UserDatabase();
        return saveUser(userSignUpReceiveModel, userDatabase);
    }

    @Override
    public BaseResponseModel edit(
            UserSignUpReceiveModel userSignUpReceiveModel,
            Integer id
    ) {
        boolean existsByUsername = userRepository
                .existsByUsernameAndIdNot(userSignUpReceiveModel.getUsername(), id);
        if (existsByUsername)
            return ERROR_USERNAME;
        Optional<UserDatabase> optionalUserDatabase =
                userRepository.findById(id);
        if (optionalUserDatabase.isEmpty())
            return ERROR_AUTH;
        UserDatabase userDatabase = optionalUserDatabase.get();
        return saveUser(userSignUpReceiveModel, userDatabase);
    }

    @Override
    public BaseResponseModel delete(Integer id) {
        try {
            userRepository.deleteById(id);
            return SUCCESS;
        } catch (Exception e) {
            return ERROR;
        }
    }

    public BaseResponseModel list() {
        SUCCESS.setObject(userRepository.findAll());
        return SUCCESS;
    }

    public UserDatabase get(Integer id) {
        Optional<UserDatabase> optionalUserDatabase = userRepository.findById(id);
        return optionalUserDatabase.orElse(null);
    }

    private BaseResponseModel saveUser(
            UserSignUpReceiveModel userSignUpReceiveModel,
            UserDatabase userDatabase
    ) {
        userDatabase.setFirstName(userSignUpReceiveModel.getFirstName());
        userDatabase.setLastName(userSignUpReceiveModel.getLastName());
        userDatabase.setUsername(userSignUpReceiveModel.getUsername());
        userDatabase.setPassword(passwordEncoder.encode(userSignUpReceiveModel.getPassword()));
        userDatabase.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.USER)));
        userRepository.save(userDatabase);
        return SUCCESS;
    }
}
