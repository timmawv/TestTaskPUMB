package avlyakulov.timur.TestTaskPUMB.dto;

import avlyakulov.timur.TestTaskPUMB.enums.AnimalSex;
import com.opencsv.bean.CsvBindByName;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AnimalRequest {

    @CsvBindByName(column = "Name")
    private String name;

    @CsvBindByName(column = "Type")
    private String type;

    @CsvBindByName(column = "Sex")
    private AnimalSex sex;

    @CsvBindByName(column = "Weight")
    private String weight;

    @CsvBindByName(column = "Cost")
    private String cost;
}