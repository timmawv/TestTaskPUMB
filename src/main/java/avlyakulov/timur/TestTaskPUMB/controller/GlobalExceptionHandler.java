package avlyakulov.timur.TestTaskPUMB.controller;

import avlyakulov.timur.TestTaskPUMB.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({FieldSortException.class, TypeSortException.class, FilterFieldException.class, CategoryNumberException.class,
            FileNotSupportedException.class, FileIsEmptyException.class})
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.badRequest().body(new ApiMessage(e.getMessage()));
    }
}