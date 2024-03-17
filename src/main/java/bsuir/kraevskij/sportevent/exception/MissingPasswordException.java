package bsuir.kraevskij.sportevent.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MissingPasswordException extends RuntimeException {
    public MissingPasswordException(String message) {
        super(message);
    }
}
