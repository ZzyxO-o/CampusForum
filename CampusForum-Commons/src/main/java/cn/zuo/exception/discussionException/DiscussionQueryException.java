package cn.zuo.exception.discussionException;

import cn.zuo.exception.BusinessException;

public class DiscussionQueryException extends BusinessException {
    public DiscussionQueryException() {
    }
    public DiscussionQueryException(String message) {
        super(message);
    }
}
