package avlyakulov.timur.TestTaskPUMB.service.impl;

import avlyakulov.timur.TestTaskPUMB.dto.AnimalResponse;
import avlyakulov.timur.TestTaskPUMB.dto.csv.AnimalRequestCSV;
import avlyakulov.timur.TestTaskPUMB.dto.xml.AnimalRequestXML;
import avlyakulov.timur.TestTaskPUMB.dto.xml.AnimalXML;
import avlyakulov.timur.TestTaskPUMB.exception.FileIsEmptyException;
import avlyakulov.timur.TestTaskPUMB.exception.FileNotSupportedException;
import avlyakulov.timur.TestTaskPUMB.mapper.AnimalMapper;
import avlyakulov.timur.TestTaskPUMB.model.Animal;
import avlyakulov.timur.TestTaskPUMB.repository.AnimalRepository;
import avlyakulov.timur.TestTaskPUMB.service.AnimalService;
import avlyakulov.timur.TestTaskPUMB.util.csv_filter.CsvEmptyFieldFilter;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
@Service
@Setter
public class AnimalServiceImpl implements AnimalService {

    private String fileIsEmpty = "Your file is empty. Please send valid files with values.";

    private String fileNotSupported = "Our application supports only .csv and .xml. Please use these file types.";

    private final String csvType = "text/csv";

    private final String xmlType = "application/xml";

    private  AnimalMapper animalMapper;

    private final AnimalRepository animalRepository;

    @Autowired
    public AnimalServiceImpl(AnimalMapper animalMapper, AnimalRepository animalRepository) {
        this.animalMapper = animalMapper;
        this.animalRepository = animalRepository;
    }


    public List<AnimalResponse> getAnimals(String fieldToSort, String type) {
        List<Animal> animals = animalRepository.findAll(Sort.by(Sort.Direction.fromString(type), fieldToSort));
        return animalMapper.mapListAnimalToAnimalResponse(animals);
    }

    public List<AnimalResponse> getAnimals(String fieldToSort) {
        List<Animal> animals = animalRepository.findAll(Sort.by(Sort.Direction.ASC, fieldToSort));
        return animalMapper.mapListAnimalToAnimalResponse(animals);
    }


    public List<AnimalResponse> getAnimals() {
        List<Animal> animals = animalRepository.findAll();
        return animalMapper.mapListAnimalToAnimalResponse(animals);
    }

    @Override
    public void parseFileToAnimalEntities(MultipartFile file) {
        validateFile(file);
        String contentType = file.getContentType();
        switch (contentType) {
            case (csvType) -> parseCsvFileAndSaveAnimals(file);
            case (xmlType) -> parseXmlFileAndSaveAnimals(file);
        }
    }

    private void parseCsvFileAndSaveAnimals(MultipartFile file) {
        List<AnimalRequestCSV> animalRequestCSV = parseToListAnimalCSVFromFile(file);
        List<Animal> animals = animalMapper.mapListAnimalCSVtoListAnimal(animalRequestCSV);
        setCategoryToAnimal(animals);
        animalRepository.saveAll(animals);
    }

    private void parseXmlFileAndSaveAnimals(MultipartFile file) {
        List<AnimalXML> animalXML = parseToListAnimalXMLFromFile(file);
        List<Animal> animals = animalMapper.mapListAnimalXMLtoListAnimal(animalXML);
        setCategoryToAnimal(animals);
        animalRepository.saveAll(animals);
    }

    private List<AnimalRequestCSV> parseToListAnimalCSVFromFile(MultipartFile file) {
        try (InputStreamReader reader = new InputStreamReader(file.getInputStream())) {
            return new CsvToBeanBuilder<AnimalRequestCSV>(reader)
                    .withType(AnimalRequestCSV.class)
                    .withIgnoreEmptyLine(true)
                    .withFilter(new CsvEmptyFieldFilter())
                    .build()
                    .parse();
        } catch (IOException e) {
            log.error("Error during reading csv file");
            throw new RuntimeException(e);
        }
    }

    private List<AnimalXML> parseToListAnimalXMLFromFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            JAXBContext jaxbContext = JAXBContext.newInstance(AnimalRequestXML.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            AnimalRequestXML animals = (AnimalRequestXML) jaxbUnmarshaller.unmarshal(inputStream);
            return animals.getAnimals().stream()
                    .filter(a -> isAnyParameterNull(a.getName(), a.getCost(), a.getWeight(), a.getType(), a.getSex()))
                    .toList();
        } catch (IOException | JAXBException e) {
            log.error("Error during reading xml file");
            throw new RuntimeException(e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (!isTypeFileValid(file))
            throw new FileNotSupportedException(fileNotSupported);

        if (file.isEmpty())
            throw new FileIsEmptyException(fileIsEmpty);
    }

    private boolean isTypeFileValid(MultipartFile file) {
        String fileType = file.getContentType();
        return (fileType.equals(csvType) || fileType.equals(xmlType));
    }

    private boolean isAnyParameterNull(String... parameters) {
        for (String parameter : parameters)
            if (parameter == null || parameter.isBlank())
                return false;
        return true;
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