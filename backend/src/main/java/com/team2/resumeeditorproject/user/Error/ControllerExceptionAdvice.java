package com.team2.resumeeditorproject.user.Error;

import com.team2.resumeeditorproject.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.swing.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("response", e.getMessage());
        response.put("time", new Date());
        response.put("status","Fail");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); //500
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleException(BadRequestException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("response", e.getMessage());
        response.put("time", new Date());
        response.put("status","Fail");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); //400
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, Object>> handleException(ForbiddenException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("response", e.getMessage());
        response.put("time", new Date());
        response.put("status","Fail");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 403
    }

    @ExceptionHandler
    public  ResponseEntity<Map<String, Object>> handleException(NotFoundException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("response", e.getMessage());
        response.put("time", new Date());
        response.put("status","Fail");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

        static class ErrorResponse {
            String message;
            Date date;

            ErrorResponse(String message, Date date) {
                this.message = message;
                this.date=date;
            }

            public String getMessage() {
                return message;
            }
        }
    }
