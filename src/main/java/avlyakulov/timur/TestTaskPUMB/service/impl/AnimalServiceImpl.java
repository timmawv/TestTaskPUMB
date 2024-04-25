package avlyakulov.timur.TestTaskPUMB.service.impl;

import avlyakulov.timur.TestTaskPUMB.dto.AnimalRequest;
import avlyakulov.timur.TestTaskPUMB.service.AnimalService;
import avlyakulov.timur.TestTaskPUMB.util.csv_filter.CsvEmptyFieldFilter;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
@Service
public class AnimalServiceImpl implements AnimalService {

    private final String csvType = "text/csv";
    private final String xmlType = "application/xml";


    @Override
    public void mapFileToAnimal(MultipartFile file) {
        String contentType = file.getContentType();
        switch (contentType) {
            case (csvType) -> parseCsvFile(file);
            case (xmlType) -> parseXmlFile(file);
        }

    }


    public void parseCsvFile(MultipartFile file) {
        try (InputStreamReader reader = new InputStreamReader(file.getInputStream())) {
            List<AnimalRequest> animals = new CsvToBeanBuilder<AnimalRequest>(reader)
                    .withType(AnimalRequest.class)
                    .withIgnoreEmptyLine(true)
                    .withFilter(new CsvEmptyFieldFilter())
                    .build()
                    .parse();

        } catch (IOException e) {
            log.error("Error during reading csv file");
            throw new RuntimeException(e);
        }
    }

    public void parseXmlFile(MultipartFile file) {
        try (InputStreamReader reader = new InputStreamReader(file.getInputStream())) {
            List<AnimalRequest> animals = new CsvToBeanBuilder<AnimalRequest>(reader)
                    .withType(AnimalRequest.class)
                    .withIgnoreEmptyLine(true)
                    .withFilter(new CsvEmptyFieldFilter())
                    .build()
                    .parse();

        } catch (IOException e) {
            log.error("Error during reading csv file");
            throw new RuntimeException(e);
        }
    }

}