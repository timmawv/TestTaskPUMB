package avlyakulov.timur.TestTaskPUMB.service;

import avlyakulov.timur.TestTaskPUMB.dto.AnimalSpecs;
import avlyakulov.timur.TestTaskPUMB.dto.RequestParamDto;
import avlyakulov.timur.TestTaskPUMB.exception.FieldSortException;
import avlyakulov.timur.TestTaskPUMB.exception.FileNotSupportedException;
import avlyakulov.timur.TestTaskPUMB.model.Animal;
import avlyakulov.timur.TestTaskPUMB.repository.AnimalRepository;
import avlyakulov.timur.TestTaskPUMB.util.category.CategoryDefiner;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;

    public List<Animal> getAnimals(RequestParamDto requestParamDto, Sort sort) {
        Specification<Animal> spec = Specification.where(null);

        if (requestParamDto.getType() != null)
            spec = spec.and(AnimalSpecs.hasType(requestParamDto.getType()));

        if (requestParamDto.getCategory() != null)
            spec = spec.and(AnimalSpecs.hasCategory(requestParamDto.getCategory()));

        if (requestParamDto.getSex() != null)
            spec = spec.and(AnimalSpecs.hasSex(requestParamDto.getSex()));

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
        CategoryDefiner categoryDefiner = new CategoryDefiner();
        animals.forEach(categoryDefiner::categorizeAnimal);
    }

    private List<Animal> validateAnimalsByParameters(List<Animal> animals) {
        SpecificationValidContext specificationValidContext = new SpecificationValidContext();
        return animals.stream()
                .filter(specificationValidContext::isAnimalValid)
                .toList();
    }
}