package com.dietre.common.aop;

import com.dietre.common.exception.BindingException;
import com.dietre.common.exception.DuplicateRowException;
import com.dietre.common.exception.IllegalActivityLevelException;
import com.dietre.common.exception.IllegalMealTimeException;
import com.dietre.common.model.response.BaseRes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final String BAD_REQUEST_MESSAGE = "입력 오류 발생";
    private final String SERVER_ERROR_MESSAGE = "서버 에러 발생";
    private final String DUPLICATED_ERROR_MESSAGE = "이미 값이 존재합니다.";

    @ExceptionHandler({NoSuchElementException.class,
            IllegalActivityLevelException.class,
            IllegalMealTimeException.class,
            BindingException.class})
    protected ResponseEntity<BaseRes> handleBadRequestExceptions(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(400).body(BaseRes.of(400, BAD_REQUEST_MESSAGE));
    }

    @ExceptionHandler(DuplicateRowException.class)
    protected ResponseEntity<BaseRes> handleDuplicateRowException(DuplicateRowException e) {
        e.printStackTrace();
        return ResponseEntity.status(400).body(BaseRes.of(400, DUPLICATED_ERROR_MESSAGE));
    }


    @ExceptionHandler(Exception.class)
    protected ResponseEntity<BaseRes> handleOtherExceptions(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(BaseRes.of(500, SERVER_ERROR_MESSAGE));
    }

}