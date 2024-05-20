package avlyakulov.timur.TestTaskPUMB.util.specification;

import avlyakulov.timur.TestTaskPUMB.entity.AnimalEntity;
import avlyakulov.timur.TestTaskPUMB.util.specification.specifitations.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


public class SpecificationContextUtil {

    private static final List<Specification<AnimalEntity>> specifications = new ArrayList<>();

    static {
        specifications.add(new CostSpecification());
        specifications.add(new NameSpecification());
        specifications.add(new SexSpecification());
        specifications.add(new TypeSpecification());
        specifications.add(new WeightSpecification());
    }

    public static boolean isAnimalValid(AnimalEntity animalEntity) {
        return specifications.stream()
                .allMatch(s -> s.isSatisfied(animalEntity));
    }
}