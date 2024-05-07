package avlyakulov.timur.TestTaskPUMB.service;

import avlyakulov.timur.TestTaskPUMB.dto.AnimalSpecs;
import avlyakulov.timur.TestTaskPUMB.exception.FieldSortException;
import avlyakulov.timur.TestTaskPUMB.exception.FileIsEmptyException;
import avlyakulov.timur.TestTaskPUMB.exception.FileNotSupportedException;
import avlyakulov.timur.TestTaskPUMB.mapper.AnimalMapper;
import avlyakulov.timur.TestTaskPUMB.model.Animal;
import avlyakulov.timur.TestTaskPUMB.repository.AnimalRepository;
import avlyakulov.timur.TestTaskPUMB.util.category.strategy.CategoryAssignmentContext;
import avlyakulov.timur.TestTaskPUMB.util.category.strategy.CategoryStrategy;
import avlyakulov.timur.TestTaskPUMB.util.file_parser.ParseFileToAnimalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AnimalService {

    private AnimalMapper animalMapper;

    private AnimalRepository animalRepository;

    private ParseFileToAnimalUtil parseFileToAnimal;

    @Autowired
    public AnimalService(AnimalMapper animalMapper, AnimalRepository animalRepository, ParseFileToAnimalUtil parseFileToAnimal) {
        this.animalMapper = animalMapper;
        this.animalRepository = animalRepository;
        this.parseFileToAnimal = parseFileToAnimal;
    }

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
        List<Animal> animals = parseFileToAnimal.parseFileToListAnimal(file);
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

    private void setCategoryToAnimal(List<Animal> animals) {
        animals.forEach(a -> a.setCategory(getCategoryByAnimalCost(a.getCost())));
    }

    private int getCategoryByAnimalCost(Integer cost) {
        CategoryAssignmentContext categoryAssignmentContext = new CategoryAssignmentContext();
        CategoryStrategy categoryStrategy = categoryAssignmentContext.defineCategoryOfAnimalByCost(cost);
        return categoryStrategy.defineCategoryByAnimalsCost().getCategory();
    }

    public void setAnimalMapper(AnimalMapper animalMapper) {
        this.animalMapper = animalMapper;
    }
}