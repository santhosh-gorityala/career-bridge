package com.jobPortal.careerBridge.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.jobPortal.careerBridge.dto.response.ErrorResponse;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request){
        Map<String,String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(),error.getDefaultMessage())
                        );
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_FAILED",
                "Request Validation Failed",
                request.getRequestURI(),
                null,
                errors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex,
            HttpServletRequest request
    ){
        ErrorResponse error =
                new ErrorResponse(
                        LocalDateTime.now(),
                        409,
                        "EMAIL_ALREADY_EXISTS",
                        ex.getMessage(),
                        request.getRequestURI(),
                        null,
                        null
                );
        return
                ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRefreshToken(
            InvalidRefreshTokenException ex,
            HttpServletRequest request
    ){
        ErrorResponse error =
                new ErrorResponse(
                  LocalDateTime.now(),
                  HttpStatus.UNAUTHORIZED.value(),
                  "INVALID_REFRESH_TOKEN",
                  ex.getMessage(),
                  request.getRequestURI(),
                  null,
                  null
                );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

    @ExceptionHandler(InvalidEmailVerificationTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEmailVerficationToken(
            InvalidEmailVerificationTokenException ex,
            HttpServletRequest request
    ){
        ErrorResponse error =
                new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.UNAUTHORIZED.value(),
                        "INVALID_EMAIL_VERIFICATION_TOKEN",
                        ex.getMessage(),
                        request.getRequestURI(),
                        null,
                        null
                );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<ErrorResponse> handleEmailSendingException(
            EmailSendingException ex,
            HttpServletRequest request) {

        ErrorResponse error =
                new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "EMAIL_SENDING_FAILED",
                        "Unable to send verification email",
                        request.getRequestURI(),
                        null,
                        null
                );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ){
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "RESOURCE_NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI(),
                null,
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(SkillAlreadyAddedException.class)
    public ResponseEntity<ErrorResponse> handleSkillAlreadyAdded(
            SkillAlreadyAddedException ex,
            HttpServletRequest request
    ){
       ErrorResponse error = new ErrorResponse(
              LocalDateTime.now(),
              HttpStatus.CONFLICT.value(),
               "SKILL_ALREADY_ADDED",
               ex.getMessage(),
               request.getRequestURI(),
               null,
               null
       ) ;
       return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(InvalidWorkExperienceException.class)
    public ResponseEntity<ErrorResponse> handleInvalidWorkExperience(
        InvalidWorkExperienceException ex,
        HttpServletRequest request
    ){
        ErrorResponse error =
                new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "INVALID_WORK_EXPERIENCE",
                        ex.getMessage(),
                        request.getRequestURI(),
                        null,
                        null
                );
        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST).
                body(error);
    }

    @ExceptionHandler(InvalidEducationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEducation(
            InvalidEducationException ex,
            HttpServletRequest request
    ){
        ErrorResponse error =
                new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "INVALID_EDUCATION_DETAILS",
                        ex.getMessage(),
                        request.getRequestURI(),
                        null,
                        null
                        );
        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST).
                body(error);
    }

    @ExceptionHandler(InvalidResumeFileException.class)
    public ResponseEntity<ErrorResponse> handleInvalidResumeFile(
            InvalidResumeFileException ex,
            HttpServletRequest request
    ){
        ErrorResponse error =
                new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "INVALID_RESUME_FILE",
                        ex.getMessage(),
                        request.getRequestURI(),
                        null,
                        null
                );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ResumeAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResumeAlreadyExists(
            ResumeAlreadyExistsException ex,
            HttpServletRequest request
    ){
        ErrorResponse error = new ErrorResponse(
          LocalDateTime.now(),
          HttpStatus.CONFLICT.value(),
          "RESUME_ALREADY_EXISTS",
                ex.getMessage(),
                request.getRequestURI(),
                null,
                null
        );
        return ResponseEntity.
                status(HttpStatus.CONFLICT).
                body(error);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRule(
            BusinessRuleException ex,
            HttpServletRequest request
    ){
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "BUSINESS_RULE_VIOLATION",
                ex.getMessage(),
                request.getRequestURI(),
                null,
                null
        );
        return ResponseEntity.
                status(HttpStatus.CONFLICT).
                body(error);
    }

    @ExceptionHandler(InvalidSearchParameterException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSearchParameter(
            InvalidSearchParameterException ex,
            HttpServletRequest request
    ){
        ErrorResponse error = new ErrorResponse(
          LocalDateTime.now(),
          HttpStatus.BAD_REQUEST.value(),
          "INVALID_SEARCH_PARAMETER",
          ex.getMessage(),
          request.getRequestURI(),
          null,
          null
        );
        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST).
                body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request
    ){

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "BAD_REQUEST",
                ex.getMessage(),
                request.getRequestURI(),
                null,
                null
        );
        return ResponseEntity.
                status(HttpStatus.BAD_REQUEST).
                body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request
    ){
        ErrorResponse error = new ErrorResponse(
          LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "FORBIDDEN",
                ex.getMessage(),
                request.getRequestURI(),
                null,
                null
        );
        return ResponseEntity.
                status(HttpStatus.FORBIDDEN).
                body(error);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDenied(
            AuthorizationDeniedException ex,
            HttpServletRequest request
    ) {

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "ACCESS_DENIED",
                "You do not have permission to access this resource",
                request.getRequestURI(),
                null,
                null
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(error);
    }

}
