package com.backend.exception;

import com.backend.payload.ApiResponceMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiResponceMessage> apiResponceMessageResponseEntity;

    //handler resource not found exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponceMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        ApiResponceMessage responceMessage = ApiResponceMessage.builder().message(ex.getMessage()).build();
        return new ResponseEntity<>(responceMessage, HttpStatus.NOT_FOUND);
    }

    //MethodArgumentNotValideException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidExcdeption(MethodArgumentNotValidException mx){
        List<ObjectError> allErrors = mx.getBindingResult().getAllErrors();

        Map<String,Object> responce=new HashMap<>();
        allErrors.stream().forEach(objectError ->{
                    String message = objectError.getDefaultMessage();
                    String field = ((FieldError) objectError).getField();
                    responce.put(field,message);
                });
        return new ResponseEntity<>(responce, HttpStatus.BAD_REQUEST);
    }
    // Handle badApiRequest
    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiResponceMessage> badApiRequestHandle(BadApiRequest ex) {
        ApiResponceMessage responceMessage = ApiResponceMessage.builder().message(ex.getMessage()).success(false).build();
        return
                apiResponceMessageResponseEntity;
    }

}
