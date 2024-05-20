package avlyakulov.timur.TestTaskPUMB.util.specification.specifitations;

import avlyakulov.timur.TestTaskPUMB.entity.AnimalEntity;
import avlyakulov.timur.TestTaskPUMB.util.specification.Specification;
import org.apache.commons.lang3.StringUtils;

public class SexSpecification implements Specification<AnimalEntity> {
    @Override
    public boolean isSatisfied(AnimalEntity item) {
        return StringUtils.isNotBlank(item.getSex());
    }
}
