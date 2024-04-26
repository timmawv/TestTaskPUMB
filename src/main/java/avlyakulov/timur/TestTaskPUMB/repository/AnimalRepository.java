package avlyakulov.timur.TestTaskPUMB.repository;

import avlyakulov.timur.TestTaskPUMB.model.Animal;
import avlyakulov.timur.TestTaskPUMB.util.specification.AnimalSpecifications;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Integer>, JpaSpecificationExecutor<Animal> {

    @Query("SELECT a FROM Animal a WHERE :type = :type")
    List<Animal> findByFilter(@Param("type") String type);

    List<Animal> findAll(Specification<Animal> spec);
}