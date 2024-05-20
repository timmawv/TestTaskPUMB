package avlyakulov.timur.TestTaskPUMB.util.specification;

import avlyakulov.timur.TestTaskPUMB.entity.AnimalEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SpecificationContextUtilTest {

    private AnimalEntity validAnimal = new AnimalEntity("buddy", "cat", "male", 30, 40, null);

    private AnimalEntity invalidAnimal = new AnimalEntity("buddy", "cat", null, 30, 40, null);

    @Test
    void isAnimalValid_animalIsValid() {
        assertThat(SpecificationContextUtil.isAnimalValid(validAnimal)).isTrue();
    }

    @Test
    void isAnimalValid_animalIsInvalid() {
        assertThat(SpecificationContextUtil.isAnimalValid(invalidAnimal)).isFalse();
    }
}