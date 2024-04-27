package avlyakulov.timur.TestTaskPUMB.util.file_parser;

import avlyakulov.timur.TestTaskPUMB.dto.csv.AnimalRequestCSV;
import avlyakulov.timur.TestTaskPUMB.dto.xml.AnimalRequestXML;
import avlyakulov.timur.TestTaskPUMB.dto.xml.AnimalXML;
import avlyakulov.timur.TestTaskPUMB.exception.FileNotSupportedException;
import avlyakulov.timur.TestTaskPUMB.mapper.AnimalMapper;
import avlyakulov.timur.TestTaskPUMB.model.Animal;
import avlyakulov.timur.TestTaskPUMB.util.csv_filter.CsvEmptyFieldFilter;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
@Component
public class ParseFileToAnimalUtil {

    private final String csvType = "text/csv";

    private final String xmlType = "text/xml";

    private AnimalMapper animalMapper;

    @Autowired
    public ParseFileToAnimalUtil(AnimalMapper animalMapper) {
        this.animalMapper = animalMapper;
    }

    public List<Animal> parseFileToListAnimal(MultipartFile file) {
        String contentType = file.getContentType();
        switch (contentType) {
            case (csvType) -> {
                return parseCsvFile(file);
            }
            case (xmlType) -> {
                return parseXmlFile(file);
            }
            default -> throw new FileNotSupportedException("Error during parsing file. File format not supported");
        }
    }

    private List<Animal> parseCsvFile(MultipartFile file) {
        List<AnimalRequestCSV> animalRequestCSV = parseToListAnimalCSVFromFile(file);
        return animalMapper.mapListAnimalCSVtoListAnimal(animalRequestCSV);
    }

    private List<Animal> parseXmlFile(MultipartFile file) {
        List<AnimalXML> animalXML = parseToListAnimalXMLFromFile(file);
        return animalMapper.mapListAnimalXMLtoListAnimal(animalXML);
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
                    .filter(a -> isParametersNotNull(a.getName(), a.getCost(), a.getWeight(), a.getType(), a.getSex()))
                    .toList();
        } catch (IOException | JAXBException e) {
            log.error("Error during reading xml file");
            throw new RuntimeException(e);
        }
    }

    private boolean isParametersNotNull(String... parameters) {
        for (String parameter : parameters)
            if (parameter == null || parameter.isBlank())
                return false;
        return true;
    }
}