package avlyakulov.timur.TestTaskPUMB.util.specification;

import avlyakulov.timur.TestTaskPUMB.model.Animal;
import org.springframework.data.jpa.domain.Specification;

public class AnimalSpecifications {
    public static Specification<Animal> filterBy(String filterField, String valueField) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(filterField), valueField);
    }
}