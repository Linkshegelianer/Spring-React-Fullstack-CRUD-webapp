package hexlet.code.exception.handler;

import hexlet.code.exception.ApiErrorResponse;
import hexlet.code.exception.JWTValidationException;
import hexlet.code.exception.NotTheOwnerException;
import hexlet.code.exception.NotFoundException;
import hexlet.code.exception.SignInException;
import hexlet.code.exception.UserAlreadyExistException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.StringJoiner;

@ControllerAdvice // Handle exceptions from all controllers
public class GlobalApiExceptionHandler /*extends ResponseEntityExceptionHandler*/ {

    // Spring Security exception from SecurityFilterChain
    @ExceptionHandler(AuthenticationException.class)
    // TODO research @ResponseBody (?)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(AuthenticationException ex,
                                                                          HttpServletRequest request) {
        ApiErrorResponse apiError = buildApiError(ex, request);
        apiError.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
        //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }

    @ExceptionHandler(JWTValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleJWTException(JWTValidationException ex,
                                                               HttpServletRequest request) {
        ApiErrorResponse apiError = buildApiError(ex, request);
        apiError.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiErrorResponse> handleJWTException(JwtException ex,
                                                               HttpServletRequest request) {
        ApiErrorResponse apiError = buildApiError(ex, request);
        apiError.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    // Spring @Valid exceptions
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleRequestValidationException(MethodArgumentNotValidException ex,
                                                                             HttpServletRequest request) {
        StringJoiner messageJoiner = new StringJoiner("; ");
        ex.getBindingResult().getFieldErrors().forEach(error -> messageJoiner.add(error.getDefaultMessage()));

        ApiErrorResponse apiError = buildApiError(ex, request);
        apiError.setStatusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
        apiError.setMessage(messageJoiner.toString());
        return new ResponseEntity<>(apiError, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ApiErrorResponse> handleRegistrationException(UserAlreadyExistException ex,
                                                                        HttpServletRequest request) {
        ApiErrorResponse apiError = buildApiError(ex, request);
        apiError.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(UsernameNotFoundException ex,
                                                                          HttpServletRequest request) {
        ApiErrorResponse apiError = buildApiError(ex, request);
        apiError.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SignInException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(SignInException ex,
                                                                          HttpServletRequest request) {
        ApiErrorResponse apiError = buildApiError(ex, request);
        apiError.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(NotFoundException ex,
                                                                    HttpServletRequest request) {
        ApiErrorResponse apiError = buildApiError(ex, request);
        apiError.setStatusCode(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(EntityNotFoundException ex,
                                                                    HttpServletRequest request) {
        ApiErrorResponse apiError = buildApiError(ex, request);
        apiError.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotTheOwnerException.class)
    public ResponseEntity<ApiErrorResponse> handleNotTheOwnerException(NotTheOwnerException ex,
                                                                       HttpServletRequest request) {
        ApiErrorResponse apiError = buildApiError(ex, request);
        apiError.setStatusCode(HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    private ApiErrorResponse buildApiError(Exception ex, HttpServletRequest request) {
        ApiErrorResponse apiError = new ApiErrorResponse();
        apiError.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiError.setMessage(ex.getMessage());
        apiError.setPath(request.getRequestURI());
        return apiError;
    }
}
