package com.yu.boot.exception;

import com.yu.common.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import java.util.List;

/**
 * 全局的的异常处理器
 * @author liuyu
 * @date 2022年11月9日17:58:44
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Resource
    private MessageSource messageSource;

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<String> handleCustomException(CustomException e) {
        //打印message中的内容
        log.error("CustomException ex={}", e.getMessage(), e);
        //获取抛出异常的code 默认为500
        int code = e.getCode();
        try {
            //获取自己写的异常原因
            String msg = e.getMsg();
            if (msg != null) {
                //不为空，进入,创建返回的参数类
                R<String> r = new R<>();
                //添加code
                r.setCode(code);
                //添加msg
                r.setMsg(msg);
                return r;
            }
        } catch (NoSuchMessageException ex) {
            log.warn("CustomException no message defined", ex);
        }
        return  R.error();
    }



    /**
     * 全局异常.
     *
     * @param e the e
     * @return R
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<String> exception(Exception e) {
        log.error("全局异常信息 ex={}", e.getMessage(), e);
        return R.error();
    }

    /**
     * validation Exception
     *
     * @param exception 报错原因
     * @return R
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<String> bodyValidExceptionHandler(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        R<String> result = new R<>();
        String msg = null;
        try {
            String messageKey = fieldErrors.get(0).getDefaultMessage();
            if (messageKey != null && messageKey.startsWith("{")) {
                messageKey = messageKey.replace("{", "");
                messageKey = messageKey.replace("}", "");
                msg = messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
            }
        } catch (NoSuchMessageException ex) {
                log.warn("validation exception no message defined", ex);
        }
        if (msg == null) {
            msg = fieldErrors.get(0).getDefaultMessage();
        }
        result.setMsg(msg);
        log.warn(fieldErrors.get(0).getDefaultMessage());
        return result;
    }
}
/**
 * 处理自定义异常
 */
/*        @ExceptionHandler(WException.class)
        @ResponseStatus(HttpStatus.OK)
        public R handleWException(WException e) {
            log.error("WException  ex={}", e.getMessage(), e);

            int code = e.getCode();
            try {
                //String msg = messageSource.getMessage(code + "", null, LocaleContextHolder.getLocale());
                String msg = e.getMsg();
                if (msg != null) {
                    R r = new R<>();
                    r.setCode(code);
                    r.setMsg(msg);
                    return r;
                }
        } catch (NoSuchMessageException ex) {
            log.warn("WException no message defined", ex);
        }

        return new R<>().error();
    }*/

/**
 * 处理未定义在property文件错误信息
 */
/*    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.OK)
    public R handleBizException(BizException e) {
        log.error("BizException  ex={}", e.getMessage(), e);
        return new R<>(e);
    }*/