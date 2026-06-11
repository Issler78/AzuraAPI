package br.com.issler.azura_api.handlers;

import br.com.issler.azura_api.exceptions.BadRequestException;
import br.com.issler.azura_api.exceptions.ErrorResponse;
import br.com.issler.azura_api.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // @Valid argument error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                // field error + message error
                .message(Objects.requireNonNull(e.getBindingResult().getFieldError()).getField() + " " + Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
