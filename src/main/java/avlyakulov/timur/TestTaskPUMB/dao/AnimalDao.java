package avlyakulov.timur.TestTaskPUMB.dao;

import avlyakulov.timur.TestTaskPUMB.exception.TypeSortException;
import avlyakulov.timur.TestTaskPUMB.model.Animal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.query.sqm.PathElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnimalDao {
    private EntityManager entityManager;

    @Autowired
    public AnimalDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Animal> getAnimals(String fieldToSort, String typeSort) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Animal> criteriaQuery = criteriaBuilder.createQuery(Animal.class);
        Root<Animal> root = criteriaQuery.from(Animal.class);
        criteriaQuery.select(root);

        addSortingToCriteriaQuery(criteriaQuery, fieldToSort, typeSort, criteriaBuilder, root);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public List<Animal> getAnimalsWithFiltering(String filterField, String filterValue, String fieldToSort, String typeSort) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Animal> criteriaQuery = criteriaBuilder.createQuery(Animal.class);
        Root<Animal> root = criteriaQuery.from(Animal.class);
        criteriaQuery.select(root);

        criteriaQuery.where(criteriaBuilder.equal(root.get(filterField), filterValue));
        addSortingToCriteriaQuery(criteriaQuery, fieldToSort, typeSort, criteriaBuilder, root);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    private void addSortingToCriteriaQuery(CriteriaQuery<Animal> criteriaQuery, String fieldToSort, String typeSort, CriteriaBuilder criteriaBuilder, Root<Animal> root) {
        if (fieldToSort != null) {
            try {
                if (typeSort != null && typeSort.equalsIgnoreCase("desc")) {
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get(fieldToSort)));
                } else if (typeSort != null && typeSort.equalsIgnoreCase("asc")) {
                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get(fieldToSort)));
                } else if (typeSort == null) {
                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get(fieldToSort)));
                } else {
                    throw new TypeSortException("Your type sort is incorrect. Please enter correct type of sort asc or desc");
                }
            } catch (PathElementException e) {
                throw new TypeSortException("Your field to sort is incorrect. Please enter correct field to sort. For instance: name or cost etc.");
            }
        }
    }
}