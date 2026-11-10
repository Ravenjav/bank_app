package by.itc.bank_app.exception.handler;

import by.itc.bank_app.dto.ErrorResponse;
import by.itc.bank_app.exception.ApplicationAlreadyCalculatedException;
import by.itc.bank_app.exception.ApplicationNotFoundException;
import by.itc.bank_app.exception.ClientNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ApplicationNotFoundException.class,
            ClientNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(
            RuntimeException ex
    ) {
        return buildResponse(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(ApplicationAlreadyCalculatedException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyCalculated(
            ApplicationAlreadyCalculatedException ex
    ) {
        return buildResponse(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex
    ) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                new IllegalArgumentException(message)
        );
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleParameterValidation(
            HandlerMethodValidationException ex
    ) {
        String message = ex.getAllValidationResults()
                .stream()
                .flatMap(result -> result.getResolvableErrors().stream())
                .map(MessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                new IllegalArgumentException(message)
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(
            HttpMessageNotReadableException ex
    ) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                new IllegalArgumentException("Некорректный формат запроса")
        );
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            RuntimeException ex
    ) {
        log.warn("Ошибка обработки запроса: {}", ex.getMessage());

        ErrorResponse response = new ErrorResponse(
                status.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(response);
    }
}
