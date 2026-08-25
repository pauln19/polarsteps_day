package org.example.polarsteps.common.error;

import io.micrometer.tracing.Tracer;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final URI VALIDATION_TYPE = URI.create("urn:polarsteps:problem:validation-failed");
    private static final URI NOT_FOUND_TYPE = URI.create("urn:polarsteps:problem:resource-not-found");
    private static final URI CONFLICT_TYPE = URI.create("urn:polarsteps:problem:conflict");
    private static final URI INTERNAL_TYPE = URI.create("urn:polarsteps:problem:internal-error");

    private final ObjectProvider<Tracer> tracer;

    public GlobalExceptionHandler(ObjectProvider<Tracer> tracer) {
        this.tracer = tracer;
    }

    public record FieldError(String field, String message) {

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<FieldError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                .toList();

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setType(VALIDATION_TYPE);
        problem.setTitle("Validation failed");
        problem.setDetail("The request body failed validation");
        problem.setProperty("errors", errors);

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {
        List<FieldError> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> new FieldError(lastNode(violation), violation.getMessage()))
                .toList();

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setType(VALIDATION_TYPE);
        problem.setTitle("Validation failed");
        problem.setDetail("One or more request parameters are invalid");
        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setType(NOT_FOUND_TYPE);
        problem.setTitle("Resource not found");
        problem.setDetail(ex.getMessage());
        return problem;
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflict(ConflictException ex) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setType(CONFLICT_TYPE);
        problem.setTitle("Conflict");
        problem.setDetail(ex.getMessage());
        return problem;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("Database constraint violated", ex);
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setType(CONFLICT_TYPE);
        problem.setTitle("Conflict");
        problem.setDetail("The request conflicts with the current state of the resource");
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex) {
        log.error("Unhandled exception", ex);
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setType(INTERNAL_TYPE);
        problem.setTitle("Internal server error");
        problem.setDetail("The request could not be completed");
        currentTraceId().ifPresent(traceId -> problem.setProperty("traceId", traceId));
        return problem;
    }

    private Optional<String> currentTraceId() {
        return Optional.ofNullable(this.tracer.getIfAvailable())
                .map(Tracer::currentSpan)
                .map(span -> span.context().traceId());
    }

    private static String lastNode(ConstraintViolation<?> violation) {
        String path = violation.getPropertyPath().toString();
        int lastDot = path.lastIndexOf('.');
        return (lastDot >= 0) ? path.substring(lastDot + 1) : path;
    }

}
