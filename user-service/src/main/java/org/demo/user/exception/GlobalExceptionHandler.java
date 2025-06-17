package org.demo.user.exception;

import lombok.extern.slf4j.Slf4j;
import org.demo.common.domain.vo.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailOrPhoneFormatException.class)
    public Result<Object> emailOrPhoneFormatException(EmailOrPhoneFormatException e) {
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(IllegalDataException.class)
    public Result<Object> illegalDataException(IllegalDataException e) {
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(IllegalOperationException.class)
    public Result<Object> illegalOperationException(IllegalOperationException e) {
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(IllegalRoleException.class)
    public Result<Object> illegalRoleException(IllegalRoleException e) {
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(PasswordFormatException.class)
    public Result<Object> passwordFormatException(PasswordFormatException e) {
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(PasswordIncorrectException.class)
    public Result<Object> passwordIncorrectException(PasswordIncorrectException e) {
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(UsernameAlreadyExistException.class)
    public Result<Object> usernameAlreadyExistException(UsernameAlreadyExistException e) {
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(UsernameOrPasswordFormatException.class)
    public Result<Object> usernameOrPasswordFormatException(UsernameOrPasswordFormatException e) {
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public Result<Object> userNotFoundException(UserNotFoundException e) {
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<Object> exception(Exception e) {
        log.error("发生错误:{}", e.getMessage(), e);
        return Result.error(e.getMessage());
    }

}
