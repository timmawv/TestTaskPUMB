package avlyakulov.timur.TestTaskPUMB.util.category.strategy;

import avlyakulov.timur.TestTaskPUMB.util.category.Category;

public class ThirdCategoryStrategy implements CategoryStrategy {

    @Override
    public Category defineCategoryByAnimalsCost() {
        return Category.THIRD_CATEGORY;
    }
}
