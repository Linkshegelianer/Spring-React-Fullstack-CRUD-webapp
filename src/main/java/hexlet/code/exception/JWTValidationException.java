package hexlet.code.exception;

public class JWTValidationException extends RuntimeException {

    public JWTValidationException(String message) {
        super(message);
    }
}
