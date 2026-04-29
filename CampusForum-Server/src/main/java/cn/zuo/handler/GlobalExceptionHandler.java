package cn.zuo.handler;


import cn.zuo.constant.exceptionConstant.SystemExceptionConstant;
import cn.zuo.exception.BusinessException;
import cn.zuo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param e
     * @return
     */
    @ExceptionHandler
    public Result<String> handleBusinessException(BusinessException e) {
        log.error("业务异常信息：{}", e.getMessage());
        return Result.error(e.getMessage());
    }

    /**
     * 捕获SQL异常
     * @param e
     * @return
     */

    @ExceptionHandler
    public Result<String> handleSQLException(SQLIntegrityConstraintViolationException e) {
        log.error("SQL异常信息:{}", e.getMessage());
        String message = e.getMessage();
        if(message.contains("Duplicate")){
            String[] split = message.split(" ");
            String msg = split[2];
            return Result.error(msg + SystemExceptionConstant.ALREADY_EXIST);
        }
        return Result.error(SystemExceptionConstant.UNKNOWN_ERROR);
    }

}
