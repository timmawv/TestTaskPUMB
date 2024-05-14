package avlyakulov.timur.TestTaskPUMB.service;

import avlyakulov.timur.TestTaskPUMB.dto.RequestParamDto;
import avlyakulov.timur.TestTaskPUMB.exception.FileNotSupportedException;
import avlyakulov.timur.TestTaskPUMB.model.Animal;
import avlyakulov.timur.TestTaskPUMB.repository.AnimalRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository;

    @Captor
    ArgumentCaptor<List<Animal>> animalCaptor;

    @InjectMocks
    private AnimalService animalService;

    private final String csvFileContent = """
            Name,Type,Sex,Weight,Cost
            Buddy,cat,female,41,78
            Cooper,,female,46,23
            Duke,cat,male,33,108
            """;

    private List<Animal> listOfCsvFileAnimals = List.of(new Animal("Buddy", "cat", "female", 41, 78, 4),
            new Animal("Duke", "cat", "male", 33, 108, 4));

    private final String xmlFileContent = """
            <?xml version="1.0" encoding="UTF-8"?>
            <animals>
                <animal>
                    <name>Milo</name>
                    <type>cat</type>
                    <sex>male</sex>
                    <weight>40</weight>
                    <cost>51</cost>
                </animal>
                <animal>
                    <type>dog</type>
                    <sex>male</sex>
                    <weight>9</weight>
                    <cost>67</cost>
                </animal>
                <animal>
                    <name>Simon</name>
                    <type>dog</type>
                    <sex>male</sex>
                    <weight>45</weight>
                    <cost>17</cost>
                </animal>
            </animals>     
            """;

    private List<Animal> listOfXmlFileAnimals = List.of(new Animal("Milo", "cat", "male", 40, 51, 3),
            new Animal("Simon", "dog", "male", 45, 17, 1));

    @Test
    void parseFile_parseCsvFile() {
        MultipartFile fileCsv = new MockMultipartFile("animals", "animals.csv", "text/csv", csvFileContent.getBytes());

        animalService.parseFile(fileCsv);

        verify(animalRepository, times(1)).saveAll(animalCaptor.capture());
        List<Animal> animalCaptorValue = animalCaptor.getValue();
        assertThat(animalCaptorValue).containsExactlyInAnyOrderElementsOf(listOfCsvFileAnimals);
    }

    @Test
    void parseFile_parseXmlFile() {
        MultipartFile fileXml = new MockMultipartFile("animals", "animals.xml", "text/xml", xmlFileContent.getBytes());

        animalService.parseFile(fileXml);

        verify(animalRepository, times(1)).saveAll(animalCaptor.capture());
        List<Animal> animalCaptorValue = animalCaptor.getValue();
        assertThat(animalCaptorValue).containsExactlyInAnyOrderElementsOf(listOfXmlFileAnimals);
    }

    @Test
    void parseFile_throwException_fileNotSupported() {
        MultipartFile fileNotSupported = new MockMultipartFile("animals", "animals.pdf", "text/pdf", csvFileContent.getBytes());

        assertThrows(FileNotSupportedException.class, () -> animalService.parseFile(fileNotSupported));
    }

    @Test
    public void testGetAnimals() {

        RequestParamDto requestParamDto = new RequestParamDto();
        requestParamDto.setType("dog");
        Sort sort = Sort.by(Sort.Direction.ASC, "name");


        Animal animal1 = new Animal();
        Animal animal2 = new Animal();
        List<Animal> animals = Arrays.asList(animal1, animal2);
        when(animalRepository.findAll(any(Specification.class), eq(sort))).thenReturn(animals);


        List<Animal> result = animalService.getAnimals(requestParamDto, sort);


        assertEquals(animals, result);
    }

    @Test
    public void testGetAnimalsWithInvalidSortField() {

        RequestParamDto requestParamDto = new RequestParamDto();
        Sort sort = Sort.by(Sort.Direction.ASC, "invalidField");


        when(animalRepository.findAll(any(Specification.class), eq(sort)))
                .thenThrow(new RuntimeException());


        assertThrows(RuntimeException.class, ()-> animalService.getAnimals(requestParamDto, sort));
    }
}