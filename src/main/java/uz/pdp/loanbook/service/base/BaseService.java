package uz.pdp.loanbook.service.base;

import uz.pdp.loanbook.model.response.BaseResponseModel;

public abstract class BaseService<T, R> {

    public abstract R add(T t);
    public abstract R edit(T t, Integer id);
    public abstract R delete(Integer id);

    protected BaseResponseModel SUCCESS = new BaseResponseModel("Success",true);
    protected BaseResponseModel ERROR = new BaseResponseModel("Error",false);
    protected BaseResponseModel ERROR_USERNAME = new BaseResponseModel("Username exists",false);
    protected BaseResponseModel ERROR_AUTH = new BaseResponseModel("User not found",false);

}

