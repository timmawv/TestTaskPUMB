package avlyakulov.timur.TestTaskPUMB.mapper;

import avlyakulov.timur.TestTaskPUMB.dto.AnimalResponse;
import avlyakulov.timur.TestTaskPUMB.dto.csv.AnimalRequestCSV;
import avlyakulov.timur.TestTaskPUMB.dto.xml.AnimalXML;
import avlyakulov.timur.TestTaskPUMB.enums.AnimalSex;
import avlyakulov.timur.TestTaskPUMB.model.Animal;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface AnimalMapper {

    List<Animal> mapListAnimalCSVtoListAnimal(List<AnimalRequestCSV> listAnimalCSV);

    List<Animal> mapListAnimalXMLtoListAnimal(List<AnimalXML> listAnimalXML);

    List<AnimalResponse> mapListAnimalToAnimalResponse(List<Animal> listAnimal);

    default AnimalSex mapStringToEnum(String sex) {
        return AnimalSex.valueOf(sex.toUpperCase());
    }
}