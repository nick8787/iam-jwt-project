package spring.security.jwt.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import spring.security.jwt.kafka.MessageProducer;
import spring.security.jwt.kafka.model.utils.ActionType;
import spring.security.jwt.kafka.model.utils.PriorityType;
import spring.security.jwt.kafka.model.utils.UtilMessage;
import spring.security.jwt.model.constants.ApiConstants;
import spring.security.jwt.model.exception.*;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
@RequiredArgsConstructor
public class CommonControllerAdvice {
    private final MessageProducer messageProducer;

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

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleInvalidPasswordException(InvalidPasswordException ex) {
        return ex.getMessage();
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
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String errorMessage = error.getDefaultMessage();
            errors.put("error", errorMessage);
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
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
        logStackTrace(ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred. Please try again later.");
    }

    private void sendLogs(String message) {
        if (Objects.nonNull(message) && message.length() > 250) {
            message = message.substring(0, 249);
        }
        messageProducer.sendLogs(UtilMessage.builder()
                .message(message)
                .actionType(ActionType.ERROR)
                .priorityType(PriorityType.HIGH)
                .build()
        );
    }

    private void logStackTrace(Exception ex) {
        StringBuilder stackTrace = new StringBuilder();
        stackTrace.append(ex.getMessage()).append(ApiConstants.BREAK_LINE);
        if (Objects.nonNull(ex.getCause())) {
            stackTrace.append(ex.getCause().getMessage()).append(ApiConstants.BREAK_LINE);
        }

        Arrays.stream(ex.getStackTrace())
                .filter(st -> st.getClassName().startsWith(ApiConstants.DEFAULT_PACKAGE_NAME) ||
                        st.getClassName().startsWith(ApiConstants.SECURITY_PACKAGE_NAME) ||
                        st.getClassName().startsWith(ApiConstants.TIME_ZONE_PACKAGE_NAME))
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
