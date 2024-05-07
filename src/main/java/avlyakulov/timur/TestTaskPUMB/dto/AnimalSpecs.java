package avlyakulov.timur.TestTaskPUMB.dto;

import avlyakulov.timur.TestTaskPUMB.model.Animal;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class AnimalSpecs {

    public static Specification<Animal> hasType(String type) {
        //we can use it as well
        //return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), type);
        return new Specification<Animal>() {
            @Override
            public Predicate toPredicate(Root<Animal> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("type")), "%".concat(type.toLowerCase()).concat("%"));
            }
        };
    }

    public static Specification<Animal> hasCategory(int category) {
        return new Specification<Animal>() {
            @Override
            public Predicate toPredicate(Root<Animal> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("category"), category);
            }
        };
    }

    public static Specification<Animal> hasSex(String sex) {
        return new Specification<Animal>() {
            @Override
            public Predicate toPredicate(Root<Animal> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(criteriaBuilder.lower(root.get("sex")), sex.toLowerCase());
            }
        };
    }
}