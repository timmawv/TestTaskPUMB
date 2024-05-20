package avlyakulov.timur.TestTaskPUMB.util.specification.specifitations;

import avlyakulov.timur.TestTaskPUMB.entity.AnimalEntity;
import avlyakulov.timur.TestTaskPUMB.util.specification.Specification;

public class TypeSpecification implements Specification<AnimalEntity> {
    @Override
    public boolean isSatisfied(AnimalEntity item) {
        return item.getType() != null && !item.getType().isBlank();
    }
}
