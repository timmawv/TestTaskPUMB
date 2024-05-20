package avlyakulov.timur.TestTaskPUMB.util.file.parser;

import avlyakulov.timur.TestTaskPUMB.entity.AnimalEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileParser {

    List<AnimalEntity> parseFileToListAnimal(MultipartFile file);
}