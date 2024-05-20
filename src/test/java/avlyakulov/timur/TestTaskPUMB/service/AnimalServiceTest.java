package avlyakulov.timur.TestTaskPUMB.service;

import avlyakulov.timur.TestTaskPUMB.dto.AnimalResponse;
import avlyakulov.timur.TestTaskPUMB.dto.FilterDto;
import avlyakulov.timur.TestTaskPUMB.entity.AnimalEntity;
import avlyakulov.timur.TestTaskPUMB.exception.FileNotSupportedException;
import avlyakulov.timur.TestTaskPUMB.repository.AnimalRepository;
import avlyakulov.timur.TestTaskPUMB.util.file.parser.FileParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private FileParser fileParser;

    @Captor
    ArgumentCaptor<List<AnimalEntity>> animalCaptor;

    @InjectMocks
    private AnimalService animalService;

    private final String csvFileContent = " ";

    private List<AnimalEntity> listOfCsvFileAnimal = List.of(new AnimalEntity("Buddy", "cat", "female", 41, 78, 4),
            new AnimalEntity("Duke", "cat", "male", 33, 108, 4));

    private final String xmlFileContent = " ";

    private List<AnimalEntity> listOfXmlFileAnimal = List.of(new AnimalEntity("Milo", "cat", "male", 40, 51, 3),
            new AnimalEntity("Simon", "dog", "male", 45, 17, 1));

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

    @Test
    public void testGetAnimals() {
        FilterDto filterDto = new FilterDto();
        filterDto.setType("dog");
        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        AnimalEntity animalEntity1 = new AnimalEntity();
        AnimalEntity animalEntity2 = new AnimalEntity();
        List<AnimalEntity> animalEntities = Arrays.asList(animalEntity1, animalEntity2);
        when(animalRepository.findAll(any(Specification.class), eq(sort))).thenReturn(animalEntities);

        List<AnimalResponse> result = animalService.getAnimals(filterDto, sort);

        assertThat(result).hasSize(2);
    }

    @Test
    public void testGetAnimalsWithInvalidSortField() {
        FilterDto filterDto = new FilterDto();
        Sort sort = Sort.by(Sort.Direction.ASC, "invalidField");


        when(animalRepository.findAll(any(Specification.class), eq(sort)))
                .thenThrow(new RuntimeException());


        assertThrows(RuntimeException.class, ()-> animalService.getAnimals(filterDto, sort));
    }
}