package org.ox.link_generator.configs;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.ox.link_generator.utils.CustomException;
import org.ox.link_generator.utils.CustomResponse;
import org.ox.link_generator.utils.ResponseMessage;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.rmi.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("Incompatible data found. Processing error response");
        Map<String, String> resData = new HashMap<>();
        List<ObjectError> errorList = ex.getBindingResult().getAllErrors();
        FieldError error1 = ((FieldError) errorList.get(0));
        errorList.forEach(err -> {
            FieldError error = (FieldError) err;
            resData.put(error.getField(), error.getDefaultMessage());
        });
        ex.printStackTrace();
        return CustomResponse.customEX(
                true, HttpStatus.UNPROCESSABLE_ENTITY,
                error1.getField() + " " + error1.getDefaultMessage(), resData, Object.class);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse
            > handleGeneralException(Exception ex) {
        ex.printStackTrace();
        return CustomResponse.error(ResponseMessage.SERVER_ERROR, null);
    }

    @ExceptionHandler(UnknownHostException.class)
    public ResponseEntity<CustomResponse> handleUnknownHostException(UnknownHostException ex) {
        log.error(":::::::::::::::::: Unknown Host");
        return CustomResponse.error(ResponseMessage.SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomResponse> handleIllegalArgException(IllegalArgumentException ex) {
        log.error(":::::::::::::::::: Invalid Arg");
        return CustomResponse.error(ResponseMessage.SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<CustomResponse> handleNullPointerException(NullPointerException ex) {
        log.error(":::::::::::::::::: Null Pointer");
        ex.printStackTrace();
        return CustomResponse.error(ResponseMessage.SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomResponse> handleCustomException(CustomException ex) {
        ex.printStackTrace();
        return CustomResponse.custom(Boolean.TRUE, ex.getStatus(), ex.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<CustomResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error(":::::::::::::::::: Mismatched Arg");
        return CustomResponse.badRequest(ResponseMessage.BAD_DATA);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<CustomResponse> handleIllegalStateException(IllegalStateException ex) {
        log.error(":::::::::::::::::: Illegal State");
        return CustomResponse.badRequest(ResponseMessage.BAD_DATA, ex.getMessage());
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<CustomResponse> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException ex) {
        log.error(":::::::::::::::::: " + ex.getMessage());
        return CustomResponse.badRequest(ex.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<CustomResponse> handleExpiredJwtException (ExpiredJwtException ex) {
        log.error(":::::::::::::::::: Expired token");
        return CustomResponse.badRequest(ResponseMessage.LINK_EXPIRED);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error(":::::::::::::::::: ConstraintViolationException");
        String msg = ex.getMessage().split("\\.")[1];
        msg = msg.split(",")[0];
        return CustomResponse.custom(
                true, HttpStatus.UNPROCESSABLE_ENTITY,
                msg, ex.getMessage());
    }
}