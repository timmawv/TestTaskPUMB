package avlyakulov.timur.TestTaskPUMB.service;

import avlyakulov.timur.TestTaskPUMB.dto.AnimalSpecs;
import avlyakulov.timur.TestTaskPUMB.exception.FieldSortException;
import avlyakulov.timur.TestTaskPUMB.exception.FileIsEmptyException;
import avlyakulov.timur.TestTaskPUMB.exception.FileNotSupportedException;
import avlyakulov.timur.TestTaskPUMB.model.Animal;
import avlyakulov.timur.TestTaskPUMB.repository.AnimalRepository;
import avlyakulov.timur.TestTaskPUMB.util.category.strategy.CategoryAssignmentContext;
import avlyakulov.timur.TestTaskPUMB.util.category.strategy.CategoryStrategy;
import avlyakulov.timur.TestTaskPUMB.util.file_parser.FileParserAnimal;
import avlyakulov.timur.TestTaskPUMB.util.file_parser.FileParserCsv;
import avlyakulov.timur.TestTaskPUMB.util.file_parser.FileParserXml;
import avlyakulov.timur.TestTaskPUMB.util.specification.SpecificationValidContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;

    private FileParserAnimal fileParserAnimal;

    public List<Animal> getAnimals(Map<String, String> searchCriteria, Sort sort) {
        Specification<Animal> spec = Specification.where(null);

        if (StringUtils.hasLength(searchCriteria.get("type")))
            spec = spec.and(AnimalSpecs.hasType(searchCriteria.get("type")));

        try {
            if (StringUtils.hasLength(searchCriteria.get("category")))
                spec = spec.and(AnimalSpecs.hasCategory(Integer.parseInt(searchCriteria.get("category"))));
        } catch (NumberFormatException ex) {
            throw new FileNotSupportedException("You have error with your field category please enter valid field for category example: 1,2,3");
        }

        if (StringUtils.hasLength(searchCriteria.get("sex")))
            spec = spec.and(AnimalSpecs.hasSex(searchCriteria.get("sex")));

        try {
            if (sort != null)
                return animalRepository.findAll(spec, sort);
            return animalRepository.findAll(spec);
        } catch (PropertyReferenceException ex) {
            throw new FieldSortException(ex.getMessage());
        }
    }

    public void parseFileToAnimalEntities(MultipartFile file) {
        validateFile(file);
        String fileType = getFileType(file);
        List<Animal> animals;
        switch (fileType) {
            case "xml" -> {
                fileParserAnimal = new FileParserXml();
                animals = fileParserAnimal.parseFileToListAnimal(file);
            }
            case "csv" -> {
                fileParserAnimal = new FileParserCsv();
                animals = fileParserAnimal.parseFileToListAnimal(file);
            }
            default -> throw new FileNotSupportedException("This type of file not supported");
        }
        animals = validateAnimalsByParameters(animals);
        setCategoryToAnimal(animals);
        animalRepository.saveAll(animals);
    }

    private void validateFile(MultipartFile file) {
        String fileType = file.getOriginalFilename();

        if (isFileNotValid(fileType))
            throw new FileNotSupportedException("Our application supports only .csv and .xml. Please use these file types.");

        if (file.isEmpty())
            throw new FileIsEmptyException("Your file is empty. Please send valid files with values.");
    }

    private boolean isFileNotValid(String fileType) {
        return !(fileType.endsWith(".csv") || fileType.endsWith(".xml"));
    }

    private String getFileType(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String[] fileType = originalFilename.split("\\.");
        return fileType[1];
    }

    private void setCategoryToAnimal(List<Animal> animals) {
        animals.forEach(a -> a.setCategory(getCategoryByAnimalCost(a.getCost())));
    }

    private int getCategoryByAnimalCost(Integer cost) {
        CategoryAssignmentContext categoryAssignmentContext = new CategoryAssignmentContext();
        CategoryStrategy categoryStrategy = categoryAssignmentContext.defineCategoryOfAnimalByCost(cost);
        return categoryStrategy.defineCategoryByAnimalsCost().getCategory();
    }

    private List<Animal> validateAnimalsByParameters(List<Animal> animals) {
        return animals.stream()
                .filter(this::isAnimalValid)
                .toList();
    }

    private boolean isAnimalValid(Animal animal) {
        SpecificationValidContext specificationValidContext = new SpecificationValidContext();
        return specificationValidContext.isAnimalValid(animal);
    }
}