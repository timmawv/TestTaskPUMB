package avlyakulov.timur.TestTaskPUMB.dto.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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