package cn.zuo.exception.replyException;

import cn.zuo.exception.BusinessException;

public class ReplyQueryException extends BusinessException {
    public ReplyQueryException() {
    }
    public ReplyQueryException(String message) {
        super(message);
    }
}