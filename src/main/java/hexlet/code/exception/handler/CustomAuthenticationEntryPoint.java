package hexlet.code.exception.handler;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // Inject Default Spring HandlerExceptionResolver
    private final HandlerExceptionResolver resolver;

    public CustomAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override // Handle exception from Spring Security Authentication (Default is Http403ForbiddenEntryPoint)
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // TODO (With @ControllerAdvice and @ExceptionHandler)
        // Delegated the handler to default resolver
        // This security exception can now be handled in @ControllerAdvice class with @ExceptionHandler method

        resolver.resolveException(request, response, null, authException);
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());

        // TODO (Without @ExceptionHandler) - just return custom response
        /*PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        out.print("{\"message\": \"custom error message\"}");
        out.flush();*/
    }
}
