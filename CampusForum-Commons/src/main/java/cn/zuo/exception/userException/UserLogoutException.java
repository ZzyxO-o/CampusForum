package cn.zuo.exception.userException;

import cn.zuo.exception.BusinessException;

public class UserLogoutException extends BusinessException {
    public UserLogoutException() {

    }
    public UserLogoutException(String message) {
        super(message);
    }
}
