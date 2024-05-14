package avlyakulov.timur.TestTaskPUMB.util.specification;

import avlyakulov.timur.TestTaskPUMB.model.Animal;
import avlyakulov.timur.TestTaskPUMB.util.specification.specifitations.*;

import java.util.ArrayList;
import java.util.List;

public class SpecificationValidContext {

    private final List<Specification<Animal>> specifications = new ArrayList<>();

    public SpecificationValidContext() {
        specifications.add(new CostSpecification());
        specifications.add(new NameSpecification());
        specifications.add(new SexSpecification());
        specifications.add(new TypeSpecification());
        specifications.add(new WeightSpecification());
    }

    public boolean isAnimalValid(Animal animal) {
        return specifications.stream()
                .allMatch(s -> s.isSatisfied(animal));
    }
}