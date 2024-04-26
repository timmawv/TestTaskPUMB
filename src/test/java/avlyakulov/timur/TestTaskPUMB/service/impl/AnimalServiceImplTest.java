package avlyakulov.timur.TestTaskPUMB.service.impl;

import avlyakulov.timur.TestTaskPUMB.dto.csv.AnimalRequestCSV;
import avlyakulov.timur.TestTaskPUMB.dto.xml.AnimalXML;
import avlyakulov.timur.TestTaskPUMB.exception.FileNotSupportedException;
import avlyakulov.timur.TestTaskPUMB.mapper.AnimalMapper;
import avlyakulov.timur.TestTaskPUMB.model.Animal;
import avlyakulov.timur.TestTaskPUMB.repository.AnimalRepository;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AnimalServiceImplTest {

    @Mock
    private AnimalRepository animalRepository;

    @Captor
    ArgumentCaptor<List<Animal>> animalCaptor;

    @InjectMocks
    private AnimalServiceImpl animalService;

    private final String csvFileContent = """
            Name,Type,Sex,Weight,Cost
            Buddy,cat,female,41,78
            Cooper,,female,46,23
            Duke,cat,male,33,108
            Rocky,dog,,18,77
            Sadie,cat,male,26,27
            Leo,cat,female,23,82
            ,cat,male,32,44
            Lola,dog,male,35,105
            Bailey,dog,male,42,46
            Loki,cat,female,11,87
            """;
    private AnimalRequestCSV animalRequestCSVFirst = new AnimalRequestCSV("Buddy", "cat", "female", "41", "78");

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
            		<name>Simon</name>
            		<type>dog</type>
            		<sex>male</sex>
            		<weight>45</weight>
            		<cost>17</cost>
            	</animal>
            	<animal>
            		<name>Molly</name>
            		<type>cat</type>
            		<sex>male</sex>
            		<weight>38</weight>
            		<cost>59</cost>
            	</animal>
            	<animal>
            		<type>dog</type>
            		<sex>male</sex>
            		<weight>9</weight>
            		<cost>67</cost>
            	</animal>
            	<animal>
            		<name>Simba</name>
            		<type>dog</type>
            		<sex>male</sex>
            		<weight>14</weight>
            		<cost>57</cost>
            	</animal>
            	<animal>
            		<name>Nala</name>
            		<type>cat</type>
            		<sex>male</sex>
            		<weight>31</weight>
            	</animal>
            	<animal>
            		<name>Toby</name>
            		<type>dog</type>
            		<sex>female</sex>
            		<weight>7</weight>
            		<cost>14</cost>
            	</animal>
            	<animal>
            		<name>Tucker</name>
            		<type>cat</type>
            		<sex>female</sex>
            		<weight>10</weight>
            		<cost>44</cost>
            	</animal>
            	<animal>
            		<name>Jack</name>
            		<type>cat</type>
            		<sex>female</sex>
            		<cost>12</cost>
            	</animal>
            	<animal>
            		<name>Zoe</name>
            		<type>cat</type>
            		<sex>male</sex>
            		<weight>30</weight>
            		<cost>49</cost>
            	</animal>
            </animals>        
            """;

    private AnimalXML animalRequestXmlFirst = new AnimalXML("Milo", "cat", "male", "40", "51");

    private final int numberValidAnimals = 7;

    MultipartFile fileCsv;
    MultipartFile fileXml;

    @BeforeEach
    void setUp() {
        fileCsv = new MockMultipartFile("animals", "animals.csv", "text/csv", csvFileContent.getBytes());
        fileXml = new MockMultipartFile("animals", "animals.xml", "application/xml", xmlFileContent.getBytes());
        animalService.setAnimalMapper(Mappers.getMapper(AnimalMapper.class));
    }

    @Test
    public void parseFileToAnimalEntities_parseCsvFile() {
        animalService.parseFileToAnimalEntities(fileCsv);
        int weight = 41;
        int cost = 78;
        int category = 4;

        verify(animalRepository, times(1)).saveAll(animalCaptor.capture());

        List<Animal> animalCaptorValue = animalCaptor.getValue();
        Animal animalFirst = animalCaptorValue.get(0);

        assertThat(animalCaptorValue).hasSize(numberValidAnimals);
        assertThat(animalFirst.getName()).isEqualTo(animalRequestCSVFirst.getName());
        assertThat(animalFirst.getType()).isEqualTo(animalRequestCSVFirst.getType());
        assertThat(animalFirst.getSex()).isEqualTo(animalRequestCSVFirst.getSex());
        assertThat(animalFirst.getWeight()).isEqualTo(weight);
        assertThat(animalFirst.getCost()).isEqualTo(cost);
        assertThat(animalFirst.getCategory()).isEqualTo(category);

    }

    @Test
    public void parseFileToAnimalEntities_parseXmlFile() {
        animalService.parseFileToAnimalEntities(fileXml);
        int weight = 40;
        int cost = 51;
        int category = 3;

        verify(animalRepository, times(1)).saveAll(animalCaptor.capture());

        List<Animal> animalCaptorValue = animalCaptor.getValue();
        Animal animalFirst = animalCaptorValue.get(0);

        assertThat(animalCaptorValue).hasSize(numberValidAnimals);
        assertThat(animalFirst.getName()).isEqualTo(animalRequestXmlFirst.getName());
        assertThat(animalFirst.getType()).isEqualTo(animalRequestXmlFirst.getType());
        assertThat(animalFirst.getSex()).isEqualTo(animalRequestXmlFirst.getSex());
        assertThat(animalFirst.getWeight()).isEqualTo(weight);
        assertThat(animalFirst.getCost()).isEqualTo(cost);
        assertThat(animalFirst.getCategory()).isEqualTo(category);
    }

    @Test
    public void parseFileToAnimalEntities_throwException_invalidFile() {
        MultipartFile invalidFile = new MockMultipartFile("animals", "animals.json", "application/json", "{}".getBytes());

        assertThrows(FileNotSupportedException.class, () -> animalService.parseFileToAnimalEntities(invalidFile));
        verify(animalRepository, times(0)).saveAll(any());
    }
}