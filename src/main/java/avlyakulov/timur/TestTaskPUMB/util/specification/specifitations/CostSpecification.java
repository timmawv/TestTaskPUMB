package avlyakulov.timur.TestTaskPUMB.util.specification.specifitations;

import avlyakulov.timur.TestTaskPUMB.model.Animal;
import avlyakulov.timur.TestTaskPUMB.util.specification.Specification;

public class CostSpecification implements Specification<Animal> {
    @Override
    public boolean isSatisfied(Animal item) {
        return item.getCost() != null;
    }
}
