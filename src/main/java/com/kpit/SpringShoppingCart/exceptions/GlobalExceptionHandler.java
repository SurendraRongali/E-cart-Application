package com.kpit.SpringShoppingCart.exceptions;

import com.kpit.SpringShoppingCart.entity.ErrorHandlingObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorHandlingObject> handleItemNotFoundException(ItemNotFoundException ex, WebRequest request){
        ErrorHandlingObject obj = new ErrorHandlingObject();
        obj.setStatusCode(HttpStatus.NOT_FOUND.value());
        obj.setMessage(ex.getMessage());
        obj.setTimestamp(new Date());

        return new ResponseEntity<ErrorHandlingObject>(obj,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorHandlingObject> handleMethodArgumentMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request){
        ErrorHandlingObject obj = new ErrorHandlingObject();
        obj.setStatusCode(HttpStatus.BAD_REQUEST.value());
        obj.setMessage(ex.getMessage());
        obj.setTimestamp(new Date());

        return new ResponseEntity<ErrorHandlingObject>(obj,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorHandlingObject> handleGeneralException(Exception ex, WebRequest request){
        ErrorHandlingObject obj = new ErrorHandlingObject();
        obj.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        obj.setMessage(ex.getMessage());
        obj.setTimestamp(new Date());

        return new ResponseEntity<ErrorHandlingObject>(obj,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ItemAlreadyExistsException.class)
    public ResponseEntity<ErrorHandlingObject> handleItemAlreadyExistsException(ItemAlreadyExistsException ex, WebRequest request){
        ErrorHandlingObject obj = new ErrorHandlingObject();
        obj.setStatusCode(HttpStatus.CONFLICT.value());
        obj.setMessage(ex.getMessage());
        obj.setTimestamp(new Date());

        return new ResponseEntity<ErrorHandlingObject>(obj,HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String,Object> body = new HashMap<>();
        body.put("timestamp",new Date());
        body.put("statusCode",HttpStatus.BAD_REQUEST.value());
        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage()).toList();
        body.put("message",errors);

        return new ResponseEntity<Object>(body,HttpStatus.BAD_REQUEST);
    }
}
