package pl.java.scalatech.errors_handle.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Locale;

import static org.springframework.http.ResponseEntity.badRequest;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalErrorHandling {

    private static final String ERROR_UNEXPECTED_EXCEPTION = "error.unexpectedException";
    private final MessageSource messageSource;

    @ResponseBody
    @ExceptionHandler({ Exception.class, RuntimeException.class })
    public ResponseEntity<String> unexpectedException(Exception e, Locale locale) {
        log.error("unhandled exception occurred : {} ", e);
        return badRequest().body(messageSource.getMessage(ERROR_UNEXPECTED_EXCEPTION, null, locale));
    }
}