package avlyakulov.timur.TestTaskPUMB.util.file.parser;

import avlyakulov.timur.TestTaskPUMB.dto.xml.AnimalRequestXML;
import avlyakulov.timur.TestTaskPUMB.dto.xml.AnimalXML;
import avlyakulov.timur.TestTaskPUMB.entity.AnimalEntity;
import avlyakulov.timur.TestTaskPUMB.mapper.AnimalMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
@Qualifier("xmlParser")
public class FileParserXml implements FileParser {

    private final AnimalMapper animalMapper = Mappers.getMapper(AnimalMapper.class);

    @Override
    public List<AnimalEntity> parseFileToListAnimal(MultipartFile file) {
        return parseXmlFile(file);
    }

    private List<AnimalEntity> parseXmlFile(MultipartFile file) {
        List<AnimalXML> animalXML = parseToListAnimalXMLFromFile(file);
        return animalMapper.mapListXMLtoListAnimal(animalXML);
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
