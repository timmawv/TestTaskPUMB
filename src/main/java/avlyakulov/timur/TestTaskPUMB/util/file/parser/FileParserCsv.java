package avlyakulov.timur.TestTaskPUMB.util.file.parser;

import avlyakulov.timur.TestTaskPUMB.dto.csv.AnimalRequestCSV;
import avlyakulov.timur.TestTaskPUMB.entity.AnimalEntity;
import avlyakulov.timur.TestTaskPUMB.mapper.AnimalMapper;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
@Component
@Qualifier("csvParser")
public class FileParserCsv implements FileParser {

    private final AnimalMapper animalMapper = Mappers.getMapper(AnimalMapper.class);

    @Override
    public List<AnimalEntity> parseFileToListAnimal(MultipartFile file) {
        return parseCsvFile(file);
    }

    private List<AnimalEntity> parseCsvFile(MultipartFile file) {
        List<AnimalRequestCSV> animalRequestCSV = parseToListAnimalCSVFromFile(file);
        return animalMapper.mapListCSVtoListAnimal(animalRequestCSV);
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