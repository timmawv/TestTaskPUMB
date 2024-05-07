package avlyakulov.timur.TestTaskPUMB.util.category.strategy;

import avlyakulov.timur.TestTaskPUMB.util.category.Category;

public class FourthCategoryStrategy implements CategoryStrategy {
    @Override
    public Category defineCategoryByAnimalsCost() {
        return Category.FOURTH_CATEGORY;
    }
}