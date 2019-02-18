package com.app.controllers;

import com.app.exception.MyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsController {
    // metoda ktora ma przechwycic dany wyjatek musi zostac
    // oznaczona za pomoca @ExceptionHandler i musisz
    // wskazac jaki wyjatek ma ta metoda przechwytywac
    @ExceptionHandler(MyException.class)
    public String myExceptionHandler(MyException e) {
        return e.getMessage();
    }
}
