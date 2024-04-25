package avlyakulov.timur.TestTaskPUMB.dto.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement(name = "animal")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnimalXML {

    private String name;

    private String type;

    private String sex;

    private String weight;

    private String cost;
}