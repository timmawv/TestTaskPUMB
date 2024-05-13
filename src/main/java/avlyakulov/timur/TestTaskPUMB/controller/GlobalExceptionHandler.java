package avlyakulov.timur.TestTaskPUMB.controller;

import avlyakulov.timur.TestTaskPUMB.dto.ApiMessageResponse;
import avlyakulov.timur.TestTaskPUMB.exception.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxUploadSize;

    @ExceptionHandler({FieldSortException.class, TypeSortException.class, FilterFieldException.class, CategoryNumberException.class,
            FileNotSupportedException.class, FileIsEmptyException.class})
    public ResponseEntity<ApiMessageResponse> handleException(Exception e) {
        return ResponseEntity.badRequest().body(new ApiMessageResponse(e.getMessage()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiMessageResponse> handleMaxSizeException() {
        return ResponseEntity.badRequest().body(new ApiMessageResponse("You file is too large. Max upload size is " + maxUploadSize));
    }
}