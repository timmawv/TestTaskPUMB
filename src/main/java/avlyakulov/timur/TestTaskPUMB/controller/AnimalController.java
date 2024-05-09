package avlyakulov.timur.TestTaskPUMB.controller;

import avlyakulov.timur.TestTaskPUMB.dto.ApiMessageResponse;
import avlyakulov.timur.TestTaskPUMB.model.Animal;
import avlyakulov.timur.TestTaskPUMB.service.AnimalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/files/uploads")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping
    public ResponseEntity<List<Animal>> getAnimals(@RequestParam Map<String, String> searchCriteria, Sort sort) {
        return ResponseEntity.ok(animalService.getAnimals(searchCriteria, sort));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAnimals(@RequestPart(value = "file") Optional<MultipartFile> file) {

        if (file.isEmpty())
            return ResponseEntity.badRequest().body(new ApiMessageResponse("You didn't add any files. Please attach at least one file."));

        animalService.parseFileToAnimalEntities(file.get());

        log.info("One file was uploaded");
        return ResponseEntity.ok(new ApiMessageResponse("File was upload and animals were saved."));
    }
}