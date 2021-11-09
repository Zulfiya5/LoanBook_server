package uz.pdp.loanbook.model.receive.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSignInReceiveModel {
    @NotNull(message = "username is mandatory")
    @Pattern(regexp = "[+][9][9][8][0-9]{9}",message = "Username must be in valid phone number format")
    private String username;

    @NotBlank(message = "password is mandatory")
    private String password;

}
