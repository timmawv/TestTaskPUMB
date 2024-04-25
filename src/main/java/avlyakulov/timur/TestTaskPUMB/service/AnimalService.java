package avlyakulov.timur.TestTaskPUMB.service;

import avlyakulov.timur.TestTaskPUMB.dto.AnimalRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AnimalService {
    void mapFileToAnimal(MultipartFile file);
}