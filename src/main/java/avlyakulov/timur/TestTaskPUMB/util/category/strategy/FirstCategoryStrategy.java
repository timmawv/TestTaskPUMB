package avlyakulov.timur.TestTaskPUMB.util.category.strategy;

import avlyakulov.timur.TestTaskPUMB.util.category.Category;

public class FirstCategoryStrategy implements CategoryStrategy {

    @Override
    public Category defineCategoryByAnimalsCost() {
        return Category.FIRST_CATEGORY;
    }
}