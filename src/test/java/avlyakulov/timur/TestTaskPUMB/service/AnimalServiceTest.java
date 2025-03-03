package avlyakulov.timur.TestTaskPUMB.service;

import avlyakulov.timur.TestTaskPUMB.dto.AnimalResponse;
import avlyakulov.timur.TestTaskPUMB.dto.FilterDto;
import avlyakulov.timur.TestTaskPUMB.entity.AnimalEntity;
import avlyakulov.timur.TestTaskPUMB.exception.FileNotSupportedException;
import avlyakulov.timur.TestTaskPUMB.mapper.AnimalMapper;
import avlyakulov.timur.TestTaskPUMB.repository.AnimalRepository;
import avlyakulov.timur.TestTaskPUMB.util.file.parser.FileParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private FileParser fileParser;

    //Проблема в том что, если мы закомментируем @Spy, то наш класс сервис по просту не видит его как бин
    @Spy
    private AnimalMapper animalMapper;

    @Captor
    ArgumentCaptor<List<AnimalEntity>> animalCaptor;

    @InjectMocks
    private AnimalService animalService;

    private final String csvFileContent = " ";

    private final List<AnimalEntity> listOfCsvFileAnimal = List.of(
            new AnimalEntity("Buddy", "cat", "female", 41, 78, 4),
            new AnimalEntity("Duke", "cat", "male", 33, 108, 4)
    );

    private final String xmlFileContent = " ";

    private final List<AnimalEntity> listOfXmlFileAnimal = List.of(
            new AnimalEntity("Milo", "cat", "male", 40, 51, 3),
            new AnimalEntity("Simon", "dog", "male", 45, 17, 1)
    );


    @Test
    void parseFile_parseCsvFile() {
        MultipartFile fileCsv = new MockMultipartFile("animal", "animal.csv", "text/csv", csvFileContent.getBytes());
        doReturn(listOfCsvFileAnimal).when(fileParser).parseFileToListAnimal(fileCsv);

        animalService.parseFile(fileCsv);

        verify(animalRepository, times(1)).saveAll(animalCaptor.capture());
        List<AnimalEntity> animalEntityCaptorValue = animalCaptor.getValue();
        assertThat(animalEntityCaptorValue).containsExactlyInAnyOrderElementsOf(listOfCsvFileAnimal);
    }


    @Test
    void parseFile_parseXmlFile() {
        MultipartFile fileXml = new MockMultipartFile("animal", "animal.xml", "text/xml", xmlFileContent.getBytes());
        doReturn(listOfXmlFileAnimal).when(fileParser).parseFileToListAnimal(fileXml);

        animalService.parseFile(fileXml);

        verify(animalRepository, times(1)).saveAll(animalCaptor.capture());
        List<AnimalEntity> animalEntityCaptorValue = animalCaptor.getValue();
        assertThat(animalEntityCaptorValue).containsExactlyInAnyOrderElementsOf(listOfXmlFileAnimal);
    }

    @Test
    void parseFile_throwException_fileNotSupported() {
        MultipartFile fileNotSupported = new MockMultipartFile("animal", "animal.pdf", "text/pdf", csvFileContent.getBytes());

        assertThrows(FileNotSupportedException.class, () -> animalService.parseFile(fileNotSupported));
    }

    //@Test
    public void testGetAnimals() {
        FilterDto filterDto = new FilterDto();
        filterDto.setType("dog");
        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        List<AnimalEntity> animalEntities = Arrays.asList(new AnimalEntity(), new AnimalEntity());
        doReturn(animalEntities).when(animalRepository).findAll(any(Specification.class), eq(sort));

        List<AnimalResponse> result = animalService.getAnimals(filterDto, sort);
        assertThat(result).hasSize(2);
    }

    @Test
    public void testGetAnimalsWithInvalidSortField() {
        FilterDto filterDto = new FilterDto();
        Sort sort = Sort.by(Sort.Direction.ASC, "invalidField");

        when(animalRepository.findAll(any(Specification.class), eq(sort)))
                .thenThrow(new RuntimeException());


        assertThrows(RuntimeException.class, () -> animalService.getAnimals(filterDto, sort));
    }
}