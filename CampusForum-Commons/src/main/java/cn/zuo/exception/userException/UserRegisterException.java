package cn.zuo.exception.userException;

import cn.zuo.exception.BusinessException;

public class UserRegisterException extends BusinessException {

    public UserRegisterException() {
    }

    public UserRegisterException(String message) {
        super(message);
    }
}
