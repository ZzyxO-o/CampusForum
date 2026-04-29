package cn.zuo.exception.userException;

import cn.zuo.exception.BusinessException;

public class UserLoginException extends BusinessException {
    public UserLoginException() {
    }

    public UserLoginException(String message) {
        super(message);
    }
}
