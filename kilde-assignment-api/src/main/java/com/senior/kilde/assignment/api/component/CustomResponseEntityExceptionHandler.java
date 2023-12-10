package com.senior.kilde.assignment.api.component;

import com.senior.kilde.assignment.api.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
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
        ServletWebRequest servlet = (ServletWebRequest) web;
        HttpServletRequest request = servlet.getRequest();
        String path = "/" + request.getContextPath() + "/" + request.getServletPath() + "/" + request.getPathInfo() + "/";
        path = path.replaceAll("/+", "/");
        path = path.substring(0, path.length() - 1);

        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(new Date());
        response.setPath(path);
        response.setStatus((short) error.getBody().getStatus());
        response.setMessage(error.getReason());
        response.setError(HttpStatus.valueOf(error.getStatusCode().value()).getReasonPhrase());
        response.setException(ResponseStatusException.class.getName());
        List<String> traces = new ArrayList<>(error.getStackTrace().length);
        StringBuilder trace = new StringBuilder();
        for (StackTraceElement element : error.getStackTrace()) {
            traces.add(element.toString());
            trace.append(element).append(System.lineSeparator());
        }
        response.setTrace(trace.toString());
        response.setTraces(traces);
        return ResponseEntity.status(error.getStatusCode()).body(response);
    }

}
