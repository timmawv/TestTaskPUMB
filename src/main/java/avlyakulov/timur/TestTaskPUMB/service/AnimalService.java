package avlyakulov.timur.TestTaskPUMB.service;

import avlyakulov.timur.TestTaskPUMB.dto.AnimalResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AnimalService {
    void mapFileToAnimal(MultipartFile file);

    List<AnimalResponse> getAnimals(String fieldToSort, String typeSort);
    List<AnimalResponse> getAnimals(String fieldToSort);
    List<AnimalResponse> getAnimals();
}