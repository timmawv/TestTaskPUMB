package avlyakulov.timur.TestTaskPUMB.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
public class FileController {

    @GetMapping("/uploads")
    public ResponseEntity<?> getFiles() {
        return ResponseEntity.ok("Hello");
    }
}