package hexlet.code;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseBody
@ControllerAdvice
public class BaseExceptionHandler {

//    @ResponseStatus(UNAUTHORIZED)
//    @ExceptionHandler(UsernameNotFoundException.class)
//    public String userNotFoundExceptionHandler(UsernameNotFoundException exception) {
//        return exception.getMessage();
//    }
//
//    @ResponseStatus(INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(Exception.class)
//    public String generalExceptionHandler(Exception exception) {
//        return exception.getMessage();
//    }
//
//    @ResponseStatus(NOT_FOUND)
//    @ExceptionHandler(NoSuchElementException.class)
//    public String noSuchElementExceptionHandler(NoSuchElementException exception) {
//        return exception.getMessage();
//    }
//
//    @ResponseStatus(BAD_REQUEST)
//    @ExceptionHandler(HttpMessageConversionException.class)
//    public String validationExceptionsHandler(Exception exception) {
//        return exception.getMessage();
//    }
//
//    @ResponseStatus(UNPROCESSABLE_ENTITY)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public List<ObjectError> validationExceptionsHandler(MethodArgumentNotValidException exception) {
//        return exception.getAllErrors();
//    }
//
//    @ResponseStatus(UNPROCESSABLE_ENTITY)
//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public String validationExceptionsHandler(DataIntegrityViolationException exception) {
//        return exception.getCause().getCause().getMessage();
//    }
//
//    @ResponseStatus(FORBIDDEN)
//    @ExceptionHandler(AccessDeniedException.class)
//    public String accessDeniedException(AccessDeniedException exception) {
//        return exception.getMessage();
//    }
}
