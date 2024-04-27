package avlyakulov.timur.TestTaskPUMB.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnimalResponse {

    private String name;

    private String type;

    private String sex;

    private Integer weight;

    private Integer cost;

    private Integer category;
}