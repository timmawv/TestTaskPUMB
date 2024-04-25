package avlyakulov.timur.TestTaskPUMB.dto.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@XmlRootElement(name = "animals")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnimalRequestXML {

    @XmlElement(name = "animal")
    List<AnimalXML> animals = new ArrayList<>();
}