package cn.zuo.exception.userException;

import cn.zuo.exception.BusinessException;

public class UserDeleteException extends BusinessException {
    public UserDeleteException() {
    }

    public UserDeleteException(String message) {
        super(message);
    }
}
