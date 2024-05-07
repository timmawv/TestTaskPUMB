package avlyakulov.timur.TestTaskPUMB.util.category.strategy;

import avlyakulov.timur.TestTaskPUMB.util.category.Category;

public class SecondCategoryStrategy implements CategoryStrategy {
    @Override
    public Category defineCategoryByAnimalsCost() {
        return Category.SECOND_CATEGORY;
    }
}
