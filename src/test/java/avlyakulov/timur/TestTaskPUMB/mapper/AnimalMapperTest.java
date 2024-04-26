package avlyakulov.timur.TestTaskPUMB.mapper;

import avlyakulov.timur.TestTaskPUMB.dto.csv.AnimalRequestCSV;
import avlyakulov.timur.TestTaskPUMB.model.Animal;
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
        List<Animal> animals = animalMapper.mapListAnimalCSVtoListAnimal(animalsCsv);
        int weight = 10;
        int cost = 55;

        AnimalRequestCSV animalCSV = animalsCsv.get(0);
        Animal animal = animals.get(0);
        assertThat(animal.getName()).isEqualTo(animalCSV.getName());
        assertThat(animal.getType()).isEqualTo(animalCSV.getType());
        assertThat(animal.getSex()).isEqualTo(animalCSV.getSex());
        assertThat(animal.getWeight()).isEqualTo(weight);
        assertThat(animal.getCost()).isEqualTo(cost);
    }
}