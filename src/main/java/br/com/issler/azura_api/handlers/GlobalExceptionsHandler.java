package br.com.issler.azura_api.handlers;

import br.com.issler.azura_api.clients.exceptions.GatewayServiceException;
import br.com.issler.azura_api.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler(GatewayServiceException.class)
    public ResponseEntity<ErrorResponse> handleGatewayServiceException(GatewayServiceException e){
        HttpStatus status = switch (e.getType()){
            case CLIENT_ERROR -> HttpStatus.BAD_REQUEST;
            case RATE_LIMITED -> HttpStatus.TOO_MANY_REQUESTS;
            case READ_TIMEOUT,
                 CONNECT_TIMEOUT,
                 CONNECTION_FAILURE,
                 SERVICE_UNAVAILABLE -> HttpStatus.BAD_GATEWAY;
            case UNKNOWN -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .status(status.value())
                .build();

        return ResponseEntity.status(status).body(errorResponse);
    }

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

    // security
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    // @Valid argument error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                // field error + message error
                .message(Objects.requireNonNull(e.getBindingResult().getFieldError()).getField() + " " + Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJsonException(HttpMessageNotReadableException e){
        // checks if the cause of error is an enum
        if (e.getMostSpecificCause() instanceof InvalidFormatException invalid && invalid.getTargetType().isEnum()) {

            String field = invalid.getPath().getFirst().getPropertyName();
            String allowedValues = Arrays.stream(invalid.getTargetType().getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));



            ErrorResponse errorResponse = ErrorResponse.builder()
                    .message(field + " should be either: " + allowedValues)
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .message("Invalid JSON")
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(CategoryInUseException.class)
    public ResponseEntity<ErrorResponse> handleCategoryInUseException(CategoryInUseException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.CONFLICT.value())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

}
