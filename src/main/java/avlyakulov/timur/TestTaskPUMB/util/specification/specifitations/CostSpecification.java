package avlyakulov.timur.TestTaskPUMB.util.specification.specifitations;

import avlyakulov.timur.TestTaskPUMB.entity.AnimalEntity;
import avlyakulov.timur.TestTaskPUMB.util.specification.Specification;

public class CostSpecification implements Specification<AnimalEntity> {
    @Override
    public boolean isSatisfied(AnimalEntity item) {
        return item.getCost() != null;
    }
}