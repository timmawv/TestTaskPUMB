package avlyakulov.timur.TestTaskPUMB.util.category.strategy;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

public class CategoryAssignmentContext {
    private final Map<Range, CategoryStrategy> strategyMap = new HashMap<>();

    public CategoryAssignmentContext() {
        strategyMap.put(new Range(0, 20), new FirstCategoryStrategy());
        strategyMap.put(new Range(21, 40), new SecondCategoryStrategy());
        strategyMap.put(new Range(41, 60), new ThirdCategoryStrategy());
        strategyMap.put(new Range(60, Integer.MAX_VALUE), new FourthCategoryStrategy());
    }

    public CategoryStrategy defineCategoryOfAnimalByCost(int cost) {
        return strategyMap.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isNumFromRange(cost))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No strategy found for cost: " + cost));
    }

    @RequiredArgsConstructor
    class Range {

        private final int min;

        private final int max;

        public boolean isNumFromRange(int value) {
            return value >= min && value <= max;
        }
    }
}
