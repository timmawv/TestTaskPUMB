package avlyakulov.timur.TestTaskPUMB.util.specification;

public interface Specification<T> {
    boolean isSatisfied(T item);
}
