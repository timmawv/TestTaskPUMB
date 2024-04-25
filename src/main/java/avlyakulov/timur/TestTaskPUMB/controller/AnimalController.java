package avlyakulov.timur.TestTaskPUMB.controller;

import avlyakulov.timur.TestTaskPUMB.service.AnimalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/files/uploads")
public class AnimalController {

    private String fileNotUpload = "File wasn't upload";

    private String fileNotAdded = "Your file is empty. Please avoid empty files";

    private String fileNotSupported = "Our application supports only .csv and .xml. Please use these file types";

    private String fileUploaded = "File was upload and animals were saved";

    private String invalidFields = "One or more params in url are invalid. Please enter valid params.";


    AnimalService animalService;

    @Autowired
    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public ResponseEntity<?> getAnimals(@RequestParam(name = "sort_by", required = false) Optional<String> fieldToSort,
                                        @RequestParam(name = "type_sort", required = false) Optional<String> typeSort) {
        if (fieldToSort.isPresent() && typeSort.isPresent())
            try {
                return ResponseEntity.ok(animalService.getAnimals(fieldToSort.get(), typeSort.get()));
            } catch (IllegalArgumentException | PropertyReferenceException e) {
                log.info("user tried to get animals with invalid params {}", e.getMessage());
                return ResponseEntity.badRequest().body(invalidFields.concat(e.getMessage()));
            }
        if (fieldToSort.isPresent())
            try {
                return ResponseEntity.ok(animalService.getAnimals(fieldToSort.get()));
            } catch (PropertyReferenceException e) {
                log.info("user tried to get animals and sort by invalid field {}", e.getMessage());
                return ResponseEntity.badRequest().body(invalidFields.concat(e.getMessage()));
            }
        return ResponseEntity.ok(animalService.getAnimals());
    }

    @PostMapping
    public ResponseEntity<?> uploadAnimals(@RequestParam(name = "file", required = false) Optional<MultipartFile> file) {
        if (file.isEmpty())
            return new ResponseEntity<>(fileNotAdded, HttpStatus.BAD_REQUEST);

        MultipartFile multipartFile = file.get();

        if (multipartFile.isEmpty())
            return new ResponseEntity<>(fileNotUpload, HttpStatus.BAD_REQUEST);

        if (!isTypeFileValid(multipartFile))
            return new ResponseEntity<>(fileNotSupported, HttpStatus.BAD_REQUEST);

        animalService.mapFileToAnimal(multipartFile);

        log.info("One file was uploaded");
        return ResponseEntity.ok(fileUploaded);
    }

    private boolean isTypeFileValid(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return (fileName != null && (fileName.endsWith(".csv") || fileName.endsWith(".xml")));
    }
}