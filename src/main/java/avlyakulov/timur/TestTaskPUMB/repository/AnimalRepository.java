package avlyakulov.timur.TestTaskPUMB.repository;

import avlyakulov.timur.TestTaskPUMB.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Integer>{

}