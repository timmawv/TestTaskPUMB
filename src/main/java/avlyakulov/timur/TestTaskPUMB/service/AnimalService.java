package avlyakulov.timur.TestTaskPUMB.service;

import avlyakulov.timur.TestTaskPUMB.dto.AnimalResponse;
import avlyakulov.timur.TestTaskPUMB.exception.*;
import avlyakulov.timur.TestTaskPUMB.mapper.AnimalMapper;
import avlyakulov.timur.TestTaskPUMB.model.Animal;
import avlyakulov.timur.TestTaskPUMB.repository.AnimalRepository;
import avlyakulov.timur.TestTaskPUMB.util.file_parser.ParseFileToAnimalUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.sqm.PathElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Slf4j
@Service
@Setter
public class AnimalService {

    private AnimalMapper animalMapper;

    private AnimalRepository animalRepository;

    private EntityManager entityManager;

    private ParseFileToAnimalUtil parseFileToAnimal;

    @Autowired
    public AnimalService(AnimalMapper animalMapper, AnimalRepository animalRepository, EntityManager entityManager, ParseFileToAnimalUtil parseFileToAnimal) {
        this.animalMapper = animalMapper;
        this.animalRepository = animalRepository;
        this.entityManager = entityManager;
        this.parseFileToAnimal = parseFileToAnimal;
    }

    //todo refactor this
    public List<AnimalResponse> getAnimals(String filterField, String filterValue, String fieldToSort, String typeSort) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Animal> criteriaQuery = criteriaBuilder.createQuery(Animal.class);
        Root<Animal> root = criteriaQuery.from(Animal.class);
        criteriaQuery.select(root);
        if (filterField != null && filterValue != null) {
            if (!"type, category, sex".contains(filterField))
                throw new FilterFieldException("Your field to filtering is incorrect. Please enter correct field to filtering. For instance: type, category, sex.");
            try {
                criteriaQuery.where(criteriaBuilder.equal(root.get(filterField), filterValue));
            } catch (RuntimeException e) {
                throw new CategoryNumberException("Your filter value for category is incorrect. Please enter correct number for filtering by category. For instance: 1, 2, 3.");
            }
        }
        if (fieldToSort != null) {
            try {
                if (typeSort != null && typeSort.equalsIgnoreCase("desc")) {
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get(fieldToSort)));
                } else if (typeSort != null && typeSort.equalsIgnoreCase("asc")) {
                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get(fieldToSort)));
                } else if (typeSort == null) {
                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get(fieldToSort)));
                } else {
                    throw new TypeSortException("Your type sort is incorrect. Please enter correct type of sort asc or desc");
                }
            } catch (PathElementException e) {
                throw new TypeSortException("Your field to sort is incorrect. Please enter correct field to sort. For instance: name or cost etc.");
            }
        }
        List<Animal> animals = entityManager.createQuery(criteriaQuery).getResultList();
        return animalMapper.mapListAnimalToAnimalResponse(animals);
    }

    public void parseFileToAnimalEntities(MultipartFile file) {
        validateFile(file);
        List<Animal> animals = parseFileToAnimal.parseFileToListAnimal(file);
        setCategoryToAnimal(animals);
        animalRepository.saveAll(animals);
    }

    private void validateFile(MultipartFile file) {
        String fileType = file.getContentType();
        if (isFileNotValid(fileType))
            throw new FileNotSupportedException("Our application supports only .csv and .xml. Please use these file types.");

        if (file.isEmpty())
            throw new FileIsEmptyException("Your file is empty. Please send valid files with values.");
    }

    private boolean isFileNotValid(String fileType) {
        return !(fileType.equals("text/csv") || fileType.equals("application/xml"));
    }

    private void setCategoryToAnimal(List<Animal> animals) {
        animals.forEach(this::defineCategoryOfAnimalByCost);
    }

    private void defineCategoryOfAnimalByCost(Animal animal) {
        Integer cost = animal.getCost();
        if (cost >= 0 && cost <= 20) {
            animal.setCategory(1);
            return;
        }
        if (cost >= 21 && cost <= 40) {
            animal.setCategory(2);
            return;
        }
        if (cost >= 41 && cost <= 60) {
            animal.setCategory(3);
            return;
        }

        if (cost >= 61)
            animal.setCategory(4);
    }
}