package avlyakulov.timur.TestTaskPUMB.controller;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/files/uploads")
public class AnimalController {

    AnimalService animalService;

    @Autowired
    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @Operation(summary = "Get list of animals", description = "Returns a list of animals that filter by field or not, sorted by field or not. If you do not specify the sort type, the default is ASC")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ApiMessage.class)))
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Bad request - User entered the wrong fields",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiMessage.class))
                    })
    })
    @GetMapping
    public ResponseEntity<?> getAnimals(@RequestParam(name = "sort_by", required = false)
                                        @Parameter(name = "sort_by", description = "field by which animals will be sorted", example = "name") String fieldToSort,
                                        @RequestParam(name = "type_sort", required = false)
                                        @Parameter(name = "type_sort", description = "type of sorting supports only asc, desc", example = "asc") String typeSort,
                                        @RequestParam(name = "filter_field", required = false)
                                        @Parameter(name = "filter_field", description = "filter field contains type, category, sex", example = "type") String filterField,
                                        @RequestParam(name = "filter_value", required = false)
                                        @Parameter(name = "filter_value", description = "filter value for filtering", example = "cat") String filterValue) {

        return ResponseEntity.ok(animalService.getAnimals(filterField, filterValue, fieldToSort, typeSort));
    }

    @Operation(summary = "Upload file to server", description = "Upload only .xml or .csv files to server.")
    @ApiResponse(responseCode = "200",
            description = "File was successfully uploaded",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiMessage.class))
            })
    @ApiResponse(responseCode = "400", description = "Bad request - User tried to upload file with wrong type or user didn't attach file",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiMessage.class))
            })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAnimals(@RequestPart(value = "file") Optional<MultipartFile> file) {

        if (file.isEmpty())
            return ResponseEntity.badRequest().body(new ApiMessage("You didn't add any files. Please attach at least one file."));

        animalService.parseFileToAnimalEntities(file.get());

        log.info("One file was uploaded");
        return ResponseEntity.ok(new ApiMessage("File was upload and animals were saved."));
    }
}