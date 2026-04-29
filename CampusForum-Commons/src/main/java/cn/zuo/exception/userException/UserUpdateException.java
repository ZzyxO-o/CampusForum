package cn.zuo.exception.userException;

import cn.zuo.exception.BusinessException;

public class UserUpdateException extends BusinessException {
    public UserUpdateException() {

    }
    public UserUpdateException(String message) {
        super(message);
    }
}
