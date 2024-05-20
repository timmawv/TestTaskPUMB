package avlyakulov.timur.TestTaskPUMB.controller;

import avlyakulov.timur.TestTaskPUMB.dto.AnimalResponse;
import avlyakulov.timur.TestTaskPUMB.dto.FilterDto;
import avlyakulov.timur.TestTaskPUMB.service.AnimalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/files/uploads")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    @GetMapping
    public ResponseEntity<List<AnimalResponse>> getAll(FilterDto filterDto, Sort sort) {
        List<AnimalResponse> animals = animalService.getAnimals(filterDto, sort);
        return ResponseEntity.ok(animals);
    }

    @PostMapping
    public ResponseEntity<List<AnimalResponse>> uploadAll(@RequestPart(name = "file") MultipartFile file) {
        List<AnimalResponse> animals = animalService.parseFile(file);
        log.info("One file was uploaded");
        return ResponseEntity.ok(animals);
    }
}