package avlyakulov.timur.TestTaskPUMB.util.specification.specifitations;

import avlyakulov.timur.TestTaskPUMB.model.Animal;
import avlyakulov.timur.TestTaskPUMB.util.specification.Specification;

public class TypeSpecification implements Specification<Animal> {
    @Override
    public boolean isSatisfied(Animal item) {
        return item.getType() != null && !item.getType().isBlank();
    }
}
