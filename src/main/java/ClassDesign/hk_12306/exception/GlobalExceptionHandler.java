package ClassDesign.hk_12306.exception;

import ClassDesign.hk_12306.pojo.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)     // 捕获所有异常
    public Result handleException(Exception e) {    // 捕获异常对象Exception
        e.printStackTrace();    // 终端打印异常堆栈信息
        return Result.error(StringUtils.hasLength(e.getMessage())? e.getMessage():"操作失败");//返回错误信息
    }
}
