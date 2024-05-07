package avlyakulov.timur.TestTaskPUMB.util.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    FIRST_CATEGORY(1),
    SECOND_CATEGORY(2),
    THIRD_CATEGORY(3),
    FOURTH_CATEGORY(4);

    private final int category;
}