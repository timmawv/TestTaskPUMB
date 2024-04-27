package avlyakulov.timur.TestTaskPUMB.util.file_parser;

import avlyakulov.timur.TestTaskPUMB.exception.FileNotSupportedException;
import avlyakulov.timur.TestTaskPUMB.mapper.AnimalMapper;
import avlyakulov.timur.TestTaskPUMB.model.Animal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ParseFileToAnimalUtilTest {

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

    private Animal animalFromCsvFile = new Animal(null, "Buddy", "cat", "female", 41, 78, null);

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

    private Animal animalFromXmlFile = new Animal(null, "Milo", "cat", "male", 40, 51, null);

    private final int numberValidAnimals = 7;

    private MultipartFile fileCsv;
    private MultipartFile fileXml;

    private ParseFileToAnimalUtil parseFileToAnimalUtil;

    private AnimalMapper animalMapper = Mappers.getMapper(AnimalMapper.class);
    ;

    @BeforeEach
    void setUp() {
        fileCsv = new MockMultipartFile("animals", "animals.csv", "text/csv", csvFileContent.getBytes());
        fileXml = new MockMultipartFile("animals", "animals.xml", "application/xml", xmlFileContent.getBytes());
        parseFileToAnimalUtil = new ParseFileToAnimalUtil(animalMapper);
    }

    @Test
    public void parseFileToAnimalEntities_parseCsvFile() {

        List<Animal> animals = parseFileToAnimalUtil.parseFileToListAnimal(fileCsv);

        assertThat(animals).hasSize(numberValidAnimals);

        Animal animal = animals.get(0);

        assertThat(animal.getId()).isNull();
        assertThat(animal.getName()).isEqualTo(animalFromCsvFile.getName());
        assertThat(animal.getType()).isEqualTo(animalFromCsvFile.getType());
        assertThat(animal.getSex()).isEqualTo(animalFromCsvFile.getSex());
        assertThat(animal.getWeight()).isEqualTo(animalFromCsvFile.getWeight());
        assertThat(animal.getCost()).isEqualTo(animalFromCsvFile.getCost());
        assertThat(animal.getCategory()).isNull();
    }

    @Test
    public void parseFileToAnimalEntities_parseXmlFile() {

        List<Animal> animals = parseFileToAnimalUtil.parseFileToListAnimal(fileXml);

        assertThat(animals).hasSize(numberValidAnimals);

        Animal animal = animals.get(0);

        assertThat(animal.getId()).isNull();
        assertThat(animal.getName()).isEqualTo(animalFromXmlFile.getName());
        assertThat(animal.getType()).isEqualTo(animalFromXmlFile.getType());
        assertThat(animal.getSex()).isEqualTo(animalFromXmlFile.getSex());
        assertThat(animal.getWeight()).isEqualTo(animalFromXmlFile.getWeight());
        assertThat(animal.getCost()).isEqualTo(animalFromXmlFile.getCost());
        assertThat(animal.getCategory()).isNull();
    }

    @Test
    public void parseFileToAnimalEntities_throwException_invalidFile() {
        MultipartFile invalidFile = new MockMultipartFile("animals", "animals.json", "application/json", "{}".getBytes());

        assertThrows(FileNotSupportedException.class, () -> parseFileToAnimalUtil.parseFileToListAnimal(invalidFile));
    }
}