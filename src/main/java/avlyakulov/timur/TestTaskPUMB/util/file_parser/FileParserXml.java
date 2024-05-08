package avlyakulov.timur.TestTaskPUMB.util.file_parser;

import avlyakulov.timur.TestTaskPUMB.dto.xml.AnimalRequestXML;
import avlyakulov.timur.TestTaskPUMB.dto.xml.AnimalXML;
import avlyakulov.timur.TestTaskPUMB.mapper.AnimalMapper;
import avlyakulov.timur.TestTaskPUMB.model.Animal;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class FileParserXml implements FileParserAnimal {

    private final AnimalMapper animalMapper = Mappers.getMapper(AnimalMapper.class);

    @Override
    public List<Animal> parseFileToListAnimal(MultipartFile file) {
        return parseXmlFile(file);
    }

    private List<Animal> parseXmlFile(MultipartFile file) {
        List<AnimalXML> animalXML = parseToListAnimalXMLFromFile(file);
        return animalMapper.mapListAnimalXMLtoListAnimal(animalXML);
    }

    private List<AnimalXML> parseToListAnimalXMLFromFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            JAXBContext jaxbContext = JAXBContext.newInstance(AnimalRequestXML.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            AnimalRequestXML animals = (AnimalRequestXML) jaxbUnmarshaller.unmarshal(inputStream);
            return animals.getAnimals();
        } catch (IOException | JAXBException e) {
            log.error("Error during reading xml file");
            throw new RuntimeException(e);
        }
    }
}
