package com.example.vortex_games.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<String> handleBadRequestException(BadRequestException badRequestException){
        return ResponseEntity.badRequest().body(badRequestException.getMessage());
    }

    @ExceptionHandler({ExistingProductException.class})
    public ResponseEntity<String> handleExistingProductException( ExistingProductException existingProductException){
        return ResponseEntity.badRequest().body(existingProductException.getMessage());
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<String> handleResourceNotFoundException( ResourceNotFoundException resourceNotFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resourceNotFoundException.getMessage());
    }


}
