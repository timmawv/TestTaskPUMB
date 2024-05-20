package avlyakulov.timur.TestTaskPUMB.mapper;

import avlyakulov.timur.TestTaskPUMB.dto.csv.AnimalRequestCSV;
import avlyakulov.timur.TestTaskPUMB.entity.AnimalEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AnimalMapperTest {

    AnimalMapper animalMapper;

    List<AnimalRequestCSV> animalsCsv;

    @BeforeEach
    void setUp() {
        animalMapper = Mappers.getMapper(AnimalMapper.class);
        animalsCsv = List.of(new AnimalRequestCSV("Jack", "cat", "male", "10", "55"));
    }

    @Test
    void mapListAnimalCSVtoListAnimal_testingMapping() {
        List<AnimalEntity> animalEntities = animalMapper.mapListCSVtoListAnimal(animalsCsv);
        int weight = 10;
        int cost = 55;

        AnimalRequestCSV animalCSV = animalsCsv.get(0);
        AnimalEntity animalEntity = animalEntities.get(0);
        assertThat(animalEntity.getName()).isEqualTo(animalCSV.getName());
        assertThat(animalEntity.getType()).isEqualTo(animalCSV.getType());
        assertThat(animalEntity.getSex()).isEqualTo(animalCSV.getSex());
        assertThat(animalEntity.getWeight()).isEqualTo(weight);
        assertThat(animalEntity.getCost()).isEqualTo(cost);
    }
}