package avlyakulov.timur.TestTaskPUMB.util.file.parser;

import avlyakulov.timur.TestTaskPUMB.entity.AnimalEntity;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FileParserTest {

    private FileParser fileParser;

    private final String csvFileContent = """
            Name,Type,Sex,Weight,Cost
            Buddy,cat,female,41,78
            Cooper,,female,46,23
            Duke,cat,male,33,108
            """;

    private final List<AnimalEntity> listOfCsvFileAnimal = List.of(new AnimalEntity("Buddy", "cat", "female", 41, 78, null),
            new AnimalEntity("Cooper", null, "female", 46, 23, null),
            new AnimalEntity("Duke", "cat", "male", 33, 108, null));

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

    private final List<AnimalEntity> listOfXmlFileAnimal = List.of(new AnimalEntity("Milo", "cat", "male", 40, 51, null),
            new AnimalEntity(null, "dog", "male", 9, 67, null),
            new AnimalEntity("Simon", "dog", "male", 45, 17, null));

    @Test
    void parseFile_parseCsvFile() {
        fileParser = new FileParserCsv();
        MultipartFile fileCsv = new MockMultipartFile("animal", "animal.csv", "text/csv", csvFileContent.getBytes());

        List<AnimalEntity> animals = fileParser.parseFileToListAnimal(fileCsv);

        assertThat(animals).containsExactlyInAnyOrderElementsOf(listOfCsvFileAnimal);
    }

    @Test
    void parseFile_parseXmlFile() {
        fileParser = new FileParserXml();
        MultipartFile fileXml = new MockMultipartFile("animal", "animal.xml", "text/xml", xmlFileContent.getBytes());

        List<AnimalEntity> animals = fileParser.parseFileToListAnimal(fileXml);

        assertThat(animals).containsExactlyInAnyOrderElementsOf(listOfXmlFileAnimal);
    }
}