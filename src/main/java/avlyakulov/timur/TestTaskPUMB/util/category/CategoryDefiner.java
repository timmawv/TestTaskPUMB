package avlyakulov.timur.TestTaskPUMB.util.category;

import avlyakulov.timur.TestTaskPUMB.exception.CategoryNumberException;
import avlyakulov.timur.TestTaskPUMB.model.Animal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CategoryDefiner {
    private final List<Map.Entry<Predicate<Animal>, Consumer<Animal>>> conditions = new ArrayList<>();

    {
        conditions.add(Map.entry(
                (animal) -> animal.getCost() >= 0 && animal.getCost() <= 20,
                (animal) -> animal.setCategory(1)
        ));

        conditions.add(Map.entry(
                (animal) -> animal.getCost() > 20 && animal.getCost() <= 40,
                (animal) -> animal.setCategory(2)
        ));

        conditions.add(Map.entry(
                (animal) -> animal.getCost() > 40 && animal.getCost() <= 60,
                (animal) -> animal.setCategory(3)
        ));

        conditions.add(Map.entry(
                (animal) -> animal.getCost() > 60,
                (animal) -> animal.setCategory(4)
        ));
    }

    public void categorizeAnimal(Animal animal) {
        conditions.stream()
                .filter(entry -> entry.getKey().test(animal))
                .findFirst()
                .ifPresentOrElse(entry -> entry.getValue().accept(animal), () -> {
                    throw new CategoryNumberException("Such condition for category doesn't exist, check animal's cost");
                });
    }
}
