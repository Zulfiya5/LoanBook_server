package uz.pdp.loanbook.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uz.pdp.loanbook.entity.user.UserDatabase;
import uz.pdp.loanbook.model.receive.user.UserSignUpReceiveModel;
import uz.pdp.loanbook.model.response.BaseResponseModel;
import uz.pdp.loanbook.security.ApplicationUsernamePasswordAuthenticationFilter;
import uz.pdp.loanbook.service.auth.AuthService;
import uz.pdp.loanbook.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final ApplicationUsernamePasswordAuthenticationFilter appAuthFilter;


    @PostMapping("/sign-up")
    public HttpEntity<?> addUser(
            @Valid @RequestBody UserSignUpReceiveModel userSignUpReceiveModel
    ) {
        BaseResponseModel response = userService.add(userSignUpReceiveModel);
        return ResponseEntity.status(response.getSuccess() ?
                HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
    }

    @PostMapping("/sign-in")
    public String sign_in(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        try {
            return appAuthFilter.successfulAuthentication(request, response);
        } catch (Exception e) {
            return "ERROR_AUTH";
        }
    }

    @GetMapping("/list")
    public HttpEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.list());
    }

    @GetMapping("/get/{username}")
    public HttpEntity<UserDatabase> getUser(
            @PathVariable String username
    ) {
        UserDatabase userDatabase = (UserDatabase) authService.loadUserByUsername(username);
        return ResponseEntity.ok(userDatabase);
    }

    @PutMapping("/edit/{id}")
    public HttpEntity<?> editUser(
            @PathVariable Integer id,
            @Valid @RequestBody UserSignUpReceiveModel userSignUpReceiveModel
    ) {
        BaseResponseModel response = userService.edit(userSignUpReceiveModel, id);
        return ResponseEntity.status(response.getSuccess() ?
                HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(response);
    }


    @DeleteMapping("/delete/{id}")
    public HttpEntity<?> deleteUser(
            @PathVariable Integer id
    ) {
        BaseResponseModel response = userService.delete(id);
        return ResponseEntity.status(response.getSuccess() ?
                HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(response);

    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


}
