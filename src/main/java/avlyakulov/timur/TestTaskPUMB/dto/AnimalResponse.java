package avlyakulov.timur.TestTaskPUMB.dto;

import avlyakulov.timur.TestTaskPUMB.enums.AnimalSex;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalResponse {

    private String name;

    private String type;

    private AnimalSex sex;

    private Integer weight;

    private Integer cost;

    private Integer category;
}