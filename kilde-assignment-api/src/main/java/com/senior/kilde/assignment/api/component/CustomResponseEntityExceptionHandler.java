package com.senior.kilde.assignment.api.component;

import com.senior.kilde.assignment.scommon.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ResponseStatusException.class})
    public ResponseEntity<ErrorResponse> handle(ResponseStatusException error, WebRequest web) {
        return commonHandle(HttpStatus.valueOf(error.getStatusCode().value()), error.getReason(), error.getClass(), error.getStackTrace(), web);
    }

    @ExceptionHandler(value = {ConcurrencyFailureException.class})
    public ResponseEntity<ErrorResponse> handle(ConcurrencyFailureException error, WebRequest web) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return commonHandle(status, error.getMessage(), error.getClass(), error.getStackTrace(), web);
    }

    protected ResponseEntity<ErrorResponse> commonHandle(HttpStatus status, String message, Class<?> clazz, StackTraceElement[] elements, WebRequest web) {
        ServletWebRequest servlet = (ServletWebRequest) web;
        HttpServletRequest request = servlet.getRequest();
        String path = "/" + request.getContextPath() + "/" + request.getServletPath() + "/" + request.getPathInfo() + "/";
        path = path.replaceAll("/+", "/");
        path = path.substring(0, path.length() - 1);

        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(new Date());
        response.setPath(path);
        response.setStatus(status.value());
        response.setMessage(message);
        response.setError(status.getReasonPhrase());
        response.setException(clazz.getName());
        List<String> traces = new ArrayList<>(elements.length);
        StringBuilder trace = new StringBuilder();
        for (StackTraceElement element : elements) {
            String text = element.toString();
            if (!text.startsWith("com.senior.kilde")) {
                continue;
            }
            traces.add(element.toString());
            trace.append(element).append(System.lineSeparator());
        }
        response.setTrace(trace.toString());
        response.setTraces(traces);
        return ResponseEntity.status(status).body(response);
    }


}
