package avlyakulov.timur.TestTaskPUMB.service;

import avlyakulov.timur.TestTaskPUMB.dto.AnimalResponse;
import avlyakulov.timur.TestTaskPUMB.dto.AnimalSpecs;
import avlyakulov.timur.TestTaskPUMB.dto.FilterDto;
import avlyakulov.timur.TestTaskPUMB.entity.AnimalEntity;
import avlyakulov.timur.TestTaskPUMB.exception.FieldSortException;
import avlyakulov.timur.TestTaskPUMB.exception.FileNotSupportedException;
import avlyakulov.timur.TestTaskPUMB.mapper.AnimalMapper;
import avlyakulov.timur.TestTaskPUMB.repository.AnimalRepository;
import avlyakulov.timur.TestTaskPUMB.util.category.CategoryUtil;
import avlyakulov.timur.TestTaskPUMB.util.file.FileType;
import avlyakulov.timur.TestTaskPUMB.util.file.FileUtil;
import avlyakulov.timur.TestTaskPUMB.util.file.parser.FileParser;
import avlyakulov.timur.TestTaskPUMB.util.specification.SpecificationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
public class AnimalService {

    private final AnimalRepository animalRepository;

    private final FileParser csvParser;

    private final FileParser xmlParser;

    private final AnimalMapper animalMapper;

    @Autowired
    public AnimalService(AnimalRepository animalRepository, @Qualifier("csvParser") FileParser csvParser, @Qualifier("xmlParser") FileParser xmlParser, AnimalMapper animalMapper) {
        this.animalRepository = animalRepository;
        this.csvParser = csvParser;
        this.xmlParser = xmlParser;
        this.animalMapper = animalMapper;
    }

    public List<AnimalResponse> getAnimals(FilterDto filterDto, Sort sort) {
        Specification<AnimalEntity> animalSpecification = configureSpecificationWithDto(filterDto);
        try {
            List<AnimalEntity> animals = animalRepository.findAll(animalSpecification, sort);
            List<AnimalResponse> animalResponses = animalMapper.mapListAnimalToListAnimalResponse(animals);
            return animalResponses;
        } catch (PropertyReferenceException ex) {
            throw new FieldSortException(ex.getMessage());
        }
    }

    public List<AnimalResponse> parseFile(MultipartFile file) {
        List<AnimalEntity> animalEntities = parseFileToAnimalEntities(file);
        animalEntities = validateAnimalsByParameters(animalEntities);
        setCategoryToAnimal(animalEntities);
        List<AnimalEntity> animals = animalRepository.saveAll(animalEntities);
        List<AnimalResponse> animalResponses = animalMapper.mapListAnimalToListAnimalResponse(animals);
        return animalResponses;
    }

    private List<AnimalEntity> parseFileToAnimalEntities(MultipartFile file) {
        FileType fileType = FileUtil.getFileType(file);
        switch (fileType) {
            case XML -> {
                return xmlParser.parseFileToListAnimal(file);
            }
            case CSV -> {
                return csvParser.parseFileToListAnimal(file);
            }
            default -> throw new FileNotSupportedException("This type of file not supported");
        }
    }

    private void setCategoryToAnimal(List<AnimalEntity> animalEntities) {
        animalEntities.forEach(CategoryUtil::categorizeAnimal);
    }

    private List<AnimalEntity> validateAnimalsByParameters(List<AnimalEntity> animalEntities) {
        return animalEntities.stream()
                .filter(SpecificationContextUtil::isAnimalValid)
                .toList();
    }

    private Specification<AnimalEntity> configureSpecificationWithDto(FilterDto filterDto) {
        Specification<AnimalEntity> spec = Specification.where(null);

        if (filterDto.getType() != null)
            spec = spec.and(AnimalSpecs.hasType(filterDto.getType()));

        if (filterDto.getCategory() != null)
            spec = spec.and(AnimalSpecs.hasCategory(filterDto.getCategory()));

        if (filterDto.getSex() != null)
            spec = spec.and(AnimalSpecs.hasSex(filterDto.getSex()));

        return spec;
    }
}