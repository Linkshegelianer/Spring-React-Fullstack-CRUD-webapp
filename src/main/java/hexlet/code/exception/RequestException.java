package hexlet.code.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bad request!")
public class RequestException extends RuntimeException {

    public RequestException() {
    }

    public RequestException(String message) {
        super(message);
    }
}
