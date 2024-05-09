package avlyakulov.timur.TestTaskPUMB.util.file.file_parser;

import avlyakulov.timur.TestTaskPUMB.dto.csv.AnimalRequestCSV;
import avlyakulov.timur.TestTaskPUMB.mapper.AnimalMapper;
import avlyakulov.timur.TestTaskPUMB.model.Animal;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
public class FileParserCsv implements FileParserAnimal {

    private final AnimalMapper animalMapper = Mappers.getMapper(AnimalMapper.class);

    @Override
    public List<Animal> parseFileToListAnimal(MultipartFile file) {
        return parseCsvFile(file);
    }

    private List<Animal> parseCsvFile(MultipartFile file) {
        List<AnimalRequestCSV> animalRequestCSV = parseToListAnimalCSVFromFile(file);
        return animalMapper.mapListAnimalCSVtoListAnimal(animalRequestCSV);
    }

    private List<AnimalRequestCSV> parseToListAnimalCSVFromFile(MultipartFile file) {
        try (InputStreamReader reader = new InputStreamReader(file.getInputStream())) {
            return new CsvToBeanBuilder<AnimalRequestCSV>(reader)
                    .withType(AnimalRequestCSV.class)
                    .withIgnoreEmptyLine(true)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.BOTH)
                    .build()
                    .parse();
        } catch (IOException e) {
            log.error("Error during reading csv file");
            throw new RuntimeException(e);
        }
    }
}