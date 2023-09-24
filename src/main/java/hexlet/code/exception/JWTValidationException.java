package hexlet.code.exception;

public class JWTValidationException extends Exception {

    public JWTValidationException(String message) {
        super(message);
    }
}
