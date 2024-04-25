package avlyakulov.timur.TestTaskPUMB.dto.csv;

import avlyakulov.timur.TestTaskPUMB.enums.AnimalSex;
import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
public class AnimalRequestCSV {

    @CsvBindByName(column = "Name")
    private String name;

    @CsvBindByName(column = "Type")
    private String type;

    @CsvBindByName(column = "Sex")
    private String sex;

    @CsvBindByName(column = "Weight")
    private String weight;

    @CsvBindByName(column = "Cost")
    private String cost;
}