package avlyakulov.timur.TestTaskPUMB.controller;

import avlyakulov.timur.TestTaskPUMB.service.AnimalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/files/uploads")
public class FileController {

    private String fileNotUpload = "File wasn't upload";

    private String fileNotAdded = "Your file is empty. Please avoid empty files";

    private String fileNotSupported = "Our application supports only .csv and .xml. Please use these file types";

    private String fileUploaded = "File was upload and animals were saved";

    AnimalService animalService;

    @Autowired
    public FileController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public ResponseEntity<?> getFiles() {
        return ResponseEntity.ok("Hello");
    }

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam(name = "file", required = false) Optional<MultipartFile> file) {
        if (file.isEmpty())
            return new ResponseEntity<>(fileNotAdded, HttpStatus.BAD_REQUEST);

        MultipartFile multipartFile = file.get();

        if (multipartFile.isEmpty())
            return new ResponseEntity<>(fileNotUpload, HttpStatus.BAD_REQUEST);

        if (!isTypeFileValid(multipartFile))
            return new ResponseEntity<>(fileNotSupported, HttpStatus.BAD_REQUEST);

        log.info("One file was uploaded");
        return ResponseEntity.ok(fileUploaded);
    }

    private boolean isTypeFileValid(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return (fileName != null && (fileName.endsWith(".csv") || fileName.endsWith(".xml")));
    }
}