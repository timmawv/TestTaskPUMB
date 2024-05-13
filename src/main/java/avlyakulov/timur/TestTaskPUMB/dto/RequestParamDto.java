package avlyakulov.timur.TestTaskPUMB.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestParamDto {

    private String type;

    private Integer category;

    private String sex;
}
