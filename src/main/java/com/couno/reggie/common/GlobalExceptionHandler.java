package com.couno.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * ClassName: GlobalExceptionHandler
 * date: 2022/11/10 19:45
 *
 * @author Couno
 * ProjectName: reggie
 */

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Rmg<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return Rmg.error(msg);
        }
        return Rmg.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    public Rmg<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());
        return Rmg.error(ex.getMessage());
    }

    @ExceptionHandler(FileNotFoundException.class)
    public Rmg<String> exceptionHandler(FileNotFoundException ex){
        log.error(ex.getMessage());
        return Rmg.error("图片加载失败");
    }

}
