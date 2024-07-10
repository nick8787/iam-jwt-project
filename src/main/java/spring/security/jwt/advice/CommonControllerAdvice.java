package spring.security.jwt.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import spring.security.jwt.model.constants.ApiConstants;
import spring.security.jwt.model.exception.BadRequestException;
import spring.security.jwt.model.exception.DataExistException;
import spring.security.jwt.model.exception.InvalidDataException;
import spring.security.jwt.model.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
@RequiredArgsConstructor
public class CommonControllerAdvice {

    @ExceptionHandler({BadRequestException.class})
    @ResponseBody
    protected ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
        logStackTrace(ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    protected ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException ex) {
        logStackTrace(ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    protected ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
        logStackTrace(ex);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    @ExceptionHandler(InvalidDataException.class)
    @ResponseBody
    protected ResponseEntity<String> handleInvalidDataException(InvalidDataException ex) {
        logStackTrace(ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    protected ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage(),
                        (oldValue, newValue) -> oldValue + ", " + newValue)
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }



    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    protected ResponseEntity<String> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        logStackTrace(ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(NotImplementedException.class)
    @ResponseBody
    protected ResponseEntity<String> handleNotImplementedException(NotImplementedException ex) {
        logStackTrace(ex);
        return ResponseEntity
                .status(HttpStatus.NOT_IMPLEMENTED)
                .body(ex.getMessage());
    }

    @ExceptionHandler(DataExistException.class)
    @ResponseBody
    protected ResponseEntity<String> handleDataExistException(DataExistException ex) {
        logStackTrace(ex);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    protected ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        logStackTrace(ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    protected ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        logStackTrace(ex);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    protected ResponseEntity<String> handleUndefinedException(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }

    private void logStackTrace(Exception ex) {
        StringBuilder stackTrace = new StringBuilder();
        stackTrace.append(ex.getMessage() ).append(ApiConstants.BREAK_LINE);
        if (Objects.nonNull(ex.getCause())) {
            stackTrace.append(ex.getCause().getMessage()).append(ApiConstants.BREAK_LINE);
        }

        String packageName;
        String exPackageName = ex.getClass().getPackageName();
        if (exPackageName.contains(ApiConstants.DEFAULT_PACKAGE_NAME)
                || exPackageName.contains(ApiConstants.SECURITY_PACKAGE_NAME)
                || exPackageName.contains(ApiConstants.TIME_ZONE_PACKAGE_NAME)
        ) {
            packageName = ApiConstants.DEFAULT_PACKAGE_NAME;
        } else {
            packageName = StringUtils.EMPTY;
        }

        Arrays.stream(ex.getStackTrace())
                .filter(st -> Objects.nonNull(st) && st.getLineNumber() > 0 && st.getClassName().contains(packageName))
                .forEach(st -> stackTrace
                        .append(ApiConstants.DEFAULT_WHITESPACES_BEFORE_STACK_TRACE)
                        .append(ApiConstants.ANSI_RED)
                        .append(st.getClassName())
                        .append(".")
                        .append(st.getMethodName())
                        .append(" (")
                        .append(st.getLineNumber())
                        .append(") ")
                        .append(ApiConstants.BREAK_LINE)
                );
        log.warn(stackTrace.append(ApiConstants.ANSI_WHITE).toString());
    }

}
