package uz.pdp.loanbook.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponseModel {

    private  String massage;
    private Boolean success;
    @JsonProperty("data")
    private Object object;

    public BaseResponseModel(Boolean success, Object object) {
        this.success = success;
        this.object = object;
    }

    public BaseResponseModel(String massage, Boolean success) {
        this.massage = massage;
        this.success = success;
    }
}
