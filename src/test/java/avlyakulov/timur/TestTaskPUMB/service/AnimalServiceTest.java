package avlyakulov.timur.TestTaskPUMB.service;

import avlyakulov.timur.TestTaskPUMB.exception.FileNotSupportedException;
import avlyakulov.timur.TestTaskPUMB.mapper.AnimalMapper;
import avlyakulov.timur.TestTaskPUMB.model.Animal;
import avlyakulov.timur.TestTaskPUMB.repository.AnimalRepository;
import avlyakulov.timur.TestTaskPUMB.util.file_parser.ParseFileToAnimalUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private ParseFileToAnimalUtil parseFileToAnimalUtil;

    @Mock
    private EntityManager entityManager;

    @Mock
    private AnimalMapper animalMapper;

    @Captor
    ArgumentCaptor<List<Animal>> animalCaptor;

    @InjectMocks
    private AnimalService animalService;

    List<Animal> animalFromCsvFileList = List.of(new Animal(null, "Buddy", "cat", "female", 41, 78, null));

    List<Animal> animalFromXmlFileList = List.of(new Animal(null, "Milo", "cat", "male", 40, 51, null));


    @Test
    public void parseFileToAnimalEntities_parseCsvFile() {
        MultipartFile fileCsv = new MockMultipartFile("animals", "animals.csv", "text/csv", "file csv".getBytes());
        Animal animalFromCsvFile = animalFromCsvFileList.get(0);
        int category = 4;
        doReturn(animalFromCsvFileList).when(parseFileToAnimalUtil).parseFileToListAnimal(fileCsv);

        animalService.parseFileToAnimalEntities(fileCsv);
        verify(animalRepository, times(1)).saveAll(animalCaptor.capture());
        List<Animal> animalCaptorValue = animalCaptor.getValue();

        Animal animal = animalCaptorValue.get(0);
        assertThat(animal.getId()).isNull();
        assertThat(animal.getName()).isEqualTo(animalFromCsvFile.getName());
        assertThat(animal.getType()).isEqualTo(animalFromCsvFile.getType());
        assertThat(animal.getSex()).isEqualTo(animalFromCsvFile.getSex());
        assertThat(animal.getWeight()).isEqualTo(animalFromCsvFile.getWeight());
        assertThat(animal.getCategory()).isEqualTo(category);
    }

    @Test
    public void parseFileToAnimalEntities_parseXmlFile() {
        MultipartFile fileXml = new MockMultipartFile("animals", "animals.xml", "application/xml", "file xml".getBytes());
        Animal animalFromXmlFile = animalFromXmlFileList.get(0);
        int category = 3;
        doReturn(animalFromXmlFileList).when(parseFileToAnimalUtil).parseFileToListAnimal(fileXml);

        animalService.parseFileToAnimalEntities(fileXml);
        verify(animalRepository, times(1)).saveAll(animalCaptor.capture());
        List<Animal> animalCaptorValue = animalCaptor.getValue();

        Animal animal = animalCaptorValue.get(0);
        assertThat(animal.getId()).isNull();
        assertThat(animal.getName()).isEqualTo(animalFromXmlFile.getName());
        assertThat(animal.getType()).isEqualTo(animalFromXmlFile.getType());
        assertThat(animal.getSex()).isEqualTo(animalFromXmlFile.getSex());
        assertThat(animal.getWeight()).isEqualTo(animalFromXmlFile.getWeight());
        assertThat(animal.getCategory()).isEqualTo(category);
    }

    @Test
    public void parseFileToAnimalEntities_throwException_invalidFile() {
        MultipartFile invalidFile = new MockMultipartFile("animals", "animals.json", "application/json", "{}".getBytes());

        assertThrows(FileNotSupportedException.class, () -> animalService.parseFileToAnimalEntities(invalidFile));
        verify(animalRepository, times(0)).saveAll(any());
    }
}