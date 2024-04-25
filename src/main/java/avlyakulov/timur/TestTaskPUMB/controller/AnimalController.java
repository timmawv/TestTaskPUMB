package avlyakulov.timur.TestTaskPUMB.controller;

import avlyakulov.timur.TestTaskPUMB.dto.AnimalResponse;
import avlyakulov.timur.TestTaskPUMB.exception.ApiMessage;
import avlyakulov.timur.TestTaskPUMB.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/files/uploads")
public class AnimalController {

    private String fileIsEmpty = "Your file is empty. Please send valid files with values.";

    private String fileNotAdded = "You didn't add any files. Please attach at least one file.";

    private String fileNotSupported = "Our application supports only .csv and .xml. Please use these file types.";

    private String fileUploaded = "File was upload and animals were saved.";

    private String invalidFields = "One or more params in url are invalid. Please enter valid params.";

    AnimalService animalService;

    @Autowired
    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @Operation(summary = "Get list of animals", description = "Returns a list of animals sorted or not")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AnimalResponse.class)))
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Bad request - User entered the wrong fields",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ApiMessage.class)))
                    })
    })
    @GetMapping
    public ResponseEntity<?> getAnimals(@RequestParam(name = "sort_by", required = false) @Parameter(name = "sort by", description = "field by which animals will be sorted", example = "name") Optional<String> fieldToSort,
                                        @RequestParam(name = "type_sort", required = false) @Parameter(name = "type sort", description = "type of sorting supports only asc, desc", example = "desc") Optional<String> typeSort) {
        if (fieldToSort.isPresent() && typeSort.isPresent())
            try {
                return ResponseEntity.ok(animalService.getAnimals(fieldToSort.get(), typeSort.get()));
            } catch (IllegalArgumentException | PropertyReferenceException e) {
                log.info("user tried to get animals with invalid params {}", e.getMessage());
                return ResponseEntity.badRequest().body(new ApiMessage(invalidFields.concat(e.getMessage())));
            }
        if (fieldToSort.isPresent())
            try {
                return ResponseEntity.ok(animalService.getAnimals(fieldToSort.get()));
            } catch (PropertyReferenceException e) {
                log.info("user tried to get animals and sort by invalid field {}", e.getMessage());
                return ResponseEntity.badRequest().body(new ApiMessage(invalidFields.concat(e.getMessage())));
            }
        return ResponseEntity.ok(animalService.getAnimals());
    }


    @Operation(summary = "Upload file to server", description = "Upload only .xml or .csv files to server")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File was successfully uploaded"),
            @ApiResponse(responseCode = "400", description = "Bad request - User tried to upload file with wrong type or user didn't attach file")
    })
    @PostMapping
    public ResponseEntity<?> uploadAnimals(@RequestParam(name = "file", required = false) Optional<MultipartFile> file) {
        if (file.isEmpty())
            return ResponseEntity.badRequest().body(new ApiMessage(fileNotAdded));

        MultipartFile multipartFile = file.get();

        if (!isTypeFileValid(multipartFile))
            return ResponseEntity.badRequest().body(new ApiMessage(fileNotSupported));

        if (multipartFile.isEmpty())
            return ResponseEntity.badRequest().body(new ApiMessage(fileIsEmpty));

        animalService.mapFileToAnimal(multipartFile);

        log.info("One file was uploaded");
        return ResponseEntity.ok(new ApiMessage(fileUploaded));
    }

    private boolean isTypeFileValid(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return (fileName != null && (fileName.endsWith(".csv") || fileName.endsWith(".xml")));
    }
}