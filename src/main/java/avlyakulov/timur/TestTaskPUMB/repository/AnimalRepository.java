package avlyakulov.timur.TestTaskPUMB.repository;

import avlyakulov.timur.TestTaskPUMB.entity.AnimalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends JpaRepository<AnimalEntity, Integer>, JpaSpecificationExecutor<AnimalEntity> {

}