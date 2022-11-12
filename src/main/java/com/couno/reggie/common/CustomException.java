package com.couno.reggie.common;

/**
 * ClassName: CustomException
 * date: 2022/11/12 14:45
 *
 * @author Couno
 * ProjectName: reggie
 */

public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
