package avlyakulov.timur.TestTaskPUMB.util.file.file_parser;

import avlyakulov.timur.TestTaskPUMB.model.Animal;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileParserAnimal {

    List<Animal> parseFileToListAnimal(MultipartFile file);
}