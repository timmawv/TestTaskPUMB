package avlyakulov.timur.TestTaskPUMB.mapper;

import avlyakulov.timur.TestTaskPUMB.dto.AnimalResponse;
import avlyakulov.timur.TestTaskPUMB.dto.csv.AnimalRequestCSV;
import avlyakulov.timur.TestTaskPUMB.dto.xml.AnimalXML;
import avlyakulov.timur.TestTaskPUMB.entity.AnimalEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface AnimalMapper {

    List<AnimalEntity> mapListCSVtoListAnimal(List<AnimalRequestCSV> listAnimalCSV);

    List<AnimalEntity> mapListXMLtoListAnimal(List<AnimalXML> listAnimalXML);

    List<AnimalResponse> mapListAnimalToListAnimalResponse(List<AnimalEntity> listAnimalEntity);
}