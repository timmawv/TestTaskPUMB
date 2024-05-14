package avlyakulov.timur.TestTaskPUMB.util.category;

import avlyakulov.timur.TestTaskPUMB.exception.CategoryNumberException;
import avlyakulov.timur.TestTaskPUMB.model.Animal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryDefinerTest {

    private CategoryDefiner categoryDefiner;

    private Animal underTest;

    @BeforeEach
    void setUp() {
        categoryDefiner = new CategoryDefiner();
        underTest = new Animal();
    }

    @ValueSource(ints = {0, 20})
    @ParameterizedTest
    void categorizeAnimal_animalSetFirstCategory(Integer cost) {
        underTest.setCost(cost);

        categoryDefiner.categorizeAnimal(underTest);

        assertThat(underTest.getCategory()).isEqualTo(1);
    }

    @ValueSource(ints = {21, 40})
    @ParameterizedTest
    void categorizeAnimal_animalSetSecondCategory(Integer cost) {
        underTest.setCost(cost);

        categoryDefiner.categorizeAnimal(underTest);

        assertThat(underTest.getCategory()).isEqualTo(2);
    }

    @ValueSource(ints = {41, 60})
    @ParameterizedTest
    void categorizeAnimal_animalSetThirdCategory(Integer cost) {
        underTest.setCost(cost);

        categoryDefiner.categorizeAnimal(underTest);

        assertThat(underTest.getCategory()).isEqualTo(3);
    }

    @ValueSource(ints = {61, 10_000})
    @ParameterizedTest
    void categorizeAnimal_animalSetFourthCategory(Integer cost) {
        underTest.setCost(cost);

        categoryDefiner.categorizeAnimal(underTest);

        assertThat(underTest.getCategory()).isEqualTo(4);
    }

    @ValueSource(ints = {-10_000, -100, -1})
    @ParameterizedTest
    void categorizeAnimal_throwException_invalidCost(Integer cost) {
        underTest.setCost(cost);

        assertThrows(CategoryNumberException.class, () -> categoryDefiner.categorizeAnimal(underTest));
    }
}