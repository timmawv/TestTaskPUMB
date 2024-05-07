package avlyakulov.timur.TestTaskPUMB.controller;

import avlyakulov.timur.TestTaskPUMB.dto.ApiMessageResponse;
import avlyakulov.timur.TestTaskPUMB.model.Animal;
import avlyakulov.timur.TestTaskPUMB.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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

    @Operation(summary = "Get list of animals", description = "Returns a list of animals that filter by field or not, sorted by field or not. If you do not specify the sort type, the default is ASC")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ApiMessageResponse.class)))
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Bad request - User entered the wrong fields",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiMessageResponse.class))
                    })
    })
    @GetMapping
    public ResponseEntity<List<Animal>> getAnimals(@RequestParam Map<String, String> searchCriteria, Pageable pageable) {
        Sort sort = pageable.getSort();
        return ResponseEntity.ok(animalService.getAnimals(searchCriteria, sort));
    }

    @Operation(summary = "Upload file to server", description = "Upload only .xml or .csv files to server.")
    @ApiResponse(responseCode = "200",
            description = "File was successfully uploaded",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiMessageResponse.class))
            })
    @ApiResponse(responseCode = "400", description = "Bad request - User tried to upload file with wrong type or user didn't attach file",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiMessageResponse.class))
            })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAnimals(@RequestPart(value = "file") Optional<MultipartFile> file) {

        if (file.isEmpty())
            return ResponseEntity.badRequest().body(new ApiMessageResponse("You didn't add any files. Please attach at least one file."));

        animalService.parseFileToAnimalEntities(file.get());

        log.info("One file was uploaded");
        return ResponseEntity.ok(new ApiMessageResponse("File was upload and animals were saved."));
    }
}