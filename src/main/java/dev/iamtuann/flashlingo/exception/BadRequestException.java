package dev.iamtuann.flashlingo.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Nullable;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
@Setter
public class BadRequestException extends RuntimeException {

    private String fieldName;
    @Nullable
    private Object fieldValue;
    public BadRequestException(String fieldName, Object fieldValue, String message) {
        super(String.format("%s cannot be '%s' : %s", fieldName, fieldValue, message));
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
