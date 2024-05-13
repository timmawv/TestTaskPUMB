package avlyakulov.timur.TestTaskPUMB.controller;

import avlyakulov.timur.TestTaskPUMB.dto.AnimalResponse;
import avlyakulov.timur.TestTaskPUMB.dto.ApiMessageResponse;
import avlyakulov.timur.TestTaskPUMB.mapper.AnimalMapper;
import avlyakulov.timur.TestTaskPUMB.model.Animal;
import avlyakulov.timur.TestTaskPUMB.service.AnimalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.WebApplicationType;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/files/uploads")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    private final AnimalMapper animalMapper;

    @GetMapping
    public ResponseEntity<List<AnimalResponse>> getAnimals(@RequestParam Map<String, String> searchCriteria, Sort sort) {
        //todo make param dto not map
        List<Animal> animals = animalService.getAnimals(searchCriteria, sort);
        return ResponseEntity.ok(animalMapper.mapListAnimalToAnimalResponse(animals));
    }

    @PostMapping
    public ResponseEntity<List<AnimalResponse>> uploadAnimals(@RequestBody MultipartFile file) {
        List<Animal> animals = animalService.parseFile(file);
        log.info("One file was uploaded");
        return ResponseEntity.ok(animalMapper.mapListAnimalToAnimalResponse(animals));
    }
}