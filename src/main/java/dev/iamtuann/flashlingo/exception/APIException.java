package dev.iamtuann.flashlingo.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class APIException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public APIException(String message, HttpStatus status, String message1) {
        super(message);
        this.message = message1;
        this.status = status;
    }

    public APIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }


}
