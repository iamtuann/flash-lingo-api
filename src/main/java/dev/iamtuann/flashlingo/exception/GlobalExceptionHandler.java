package dev.iamtuann.flashlingo.exception;

import dev.iamtuann.flashlingo.model.ErrorDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetail> handleResourceNotFoundException(ResourceNotFoundException exception) {
        ErrorDetail errorDetail = new ErrorDetail(new Date(), exception.getMessage());
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorDetail> handleAPIException(APIException exception) {
        ErrorDetail errorDetail = new ErrorDetail(new Date(), exception.getMessage());
        return new ResponseEntity<>(errorDetail, exception.getStatus());
    }

    @ExceptionHandler(NoPermissionException.class)
    public ResponseEntity<ErrorDetail> handleNoAccessException(NoPermissionException exception) {
        ErrorDetail errorDetail = new ErrorDetail(new Date(), exception.getMessage());
        return new ResponseEntity<>(errorDetail, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDetail> handleBadRequestException(BadRequestException exception) {
        ErrorDetail errorDetail = new ErrorDetail(new Date(), exception.getMessage());
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> handleGlobalException(Exception exception){
        ErrorDetail errorDetails = new ErrorDetail(new Date(), exception.getMessage());
        logger.error("", exception);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
