package cn.zuo.exception.userException;

import cn.zuo.exception.BusinessException;

public class UserChangePasswordException extends BusinessException {
    public UserChangePasswordException() {
    }

    public UserChangePasswordException(String message) {
        super(message);
    }
}
