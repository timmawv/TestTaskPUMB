package avlyakulov.timur.TestTaskPUMB.service;

import avlyakulov.timur.TestTaskPUMB.dto.AnimalSpecs;
import avlyakulov.timur.TestTaskPUMB.exception.FieldSortException;
import avlyakulov.timur.TestTaskPUMB.exception.FileNotSupportedException;
import avlyakulov.timur.TestTaskPUMB.model.Animal;
import avlyakulov.timur.TestTaskPUMB.repository.AnimalRepository;
import avlyakulov.timur.TestTaskPUMB.util.category.strategy.CategoryAssignmentContext;
import avlyakulov.timur.TestTaskPUMB.util.category.strategy.CategoryStrategy;
import avlyakulov.timur.TestTaskPUMB.util.file.FileType;
import avlyakulov.timur.TestTaskPUMB.util.file.FileUtil;
import avlyakulov.timur.TestTaskPUMB.util.file.file_parser.FileParserAnimal;
import avlyakulov.timur.TestTaskPUMB.util.file.file_parser.FileParserCsv;
import avlyakulov.timur.TestTaskPUMB.util.file.file_parser.FileParserXml;
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
            return animalRepository.findAll(spec, sort);
        } catch (PropertyReferenceException ex) {
            throw new FieldSortException(ex.getMessage());
        }
    }

    public List<Animal> parseFile(MultipartFile file) {
        List<Animal> animals = parseFileToAnimalEntities(file);
        animals = validateAnimalsByParameters(animals);
        setCategoryToAnimal(animals);
        return animalRepository.saveAll(animals);
    }

    private List<Animal> parseFileToAnimalEntities(MultipartFile file) {
        FileUtil fileUtil = new FileUtil();
        FileType fileType = fileUtil.getFileType(file);
        FileParserAnimal fileParserAnimal;
        switch (fileType) {
            case XML -> {
                fileParserAnimal = new FileParserXml();
                return fileParserAnimal.parseFileToListAnimal(file);
            }
            case CSV -> {
                fileParserAnimal = new FileParserCsv();
                return fileParserAnimal.parseFileToListAnimal(file);
            }
            default -> throw new FileNotSupportedException("This type of file not supported");
        }
    }

    private void setCategoryToAnimal(List<Animal> animals) {
        CategoryAssignmentContext categoryAssignmentContext = new CategoryAssignmentContext();
        animals.forEach(a -> a.setCategory(getCategoryByAnimalCost(categoryAssignmentContext, a.getCost())));
    }

    private int getCategoryByAnimalCost(CategoryAssignmentContext categoryAssignmentContext, Integer cost) {
        CategoryStrategy categoryStrategy = categoryAssignmentContext.defineCategoryOfAnimalByCost(cost);
        return categoryStrategy.defineCategoryByAnimalsCost().getCategory();
    }

    private List<Animal> validateAnimalsByParameters(List<Animal> animals) {
        SpecificationValidContext specificationValidContext = new SpecificationValidContext();
        return animals.stream()
                .filter(a -> isAnimalValid(specificationValidContext, a))
                .toList();
    }

    private boolean isAnimalValid(SpecificationValidContext specificationValidContext, Animal animal) {
        return specificationValidContext.isAnimalValid(animal);
    }
}