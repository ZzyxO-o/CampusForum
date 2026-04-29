package cn.zuo.exception.discussionException;

import cn.zuo.exception.BusinessException;

public class DiscussionDeleteException extends BusinessException {
    public DiscussionDeleteException() {}
    public DiscussionDeleteException(String message) {
        super(message);
    }

}
