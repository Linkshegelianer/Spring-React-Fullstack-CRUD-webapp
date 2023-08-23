package hexlet.code.exception;

import org.springframework.security.access.AccessDeniedException;

public class NotTheOwnerException extends AccessDeniedException {

    public NotTheOwnerException(String message) {
        super(message);
    }
}
