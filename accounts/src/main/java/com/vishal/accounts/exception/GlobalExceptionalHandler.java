package com.vishal.accounts.exception;

import com.vishal.accounts.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionalHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exception,
                                                                  WebRequest webRequest) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                            WebRequest webRequest) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException exception,
                                                                                 WebRequest webRequest){
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateMobileNumberException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateMobileNumberException(DuplicateMobileNumberException exception,
                                                                                 WebRequest webRequest){
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.CONFLICT,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {
        String dbMessage = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();

        ConstraintViolation violation = ConstraintViolation.fromMessage(dbMessage);
        String message = ConstraintViolation.resolveFriendlyMessage(dbMessage);

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                "uri=" + request.getRequestURI(),
                violation.getStatus(),
                message,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, violation.getStatus());
    }

    private static final Map<String, String> CONSTRAINT_MESSAGES = Map.of(
            "mobile_number_UNIQUE", "Duplicate mobile number. This number already exists.",
            "email_UNIQUE", "Duplicate email. This email is already in use."
    );

    private String extractMeaningfulMessage(String message) {
        if (message.contains("Duplicate entry")) {
            for (Map.Entry<String, String> entry : CONSTRAINT_MESSAGES.entrySet()) {
                if (message.contains(entry.getKey())) {
                    return entry.getValue();
                }
            }
            return "Duplicate entry. A unique constraint was violated.";
        }
        return message;
    }



//    @ExceptionHandler(NoResourceFoundException.class)
//    public ResponseEntity<ErrorResponseDto> handleNoResourceFoundException(NoResourceFoundException exception,
//                                                                            WebRequest webRequest) {
//        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
//                webRequest.getDescription(false),
//                HttpStatus.NOT_FOUND,
//                exception.getMessage(),
//                LocalDateTime.now()
//        );
//        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
//    }
}
