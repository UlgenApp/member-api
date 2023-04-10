package tr.edu.ku.ulgen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tr.edu.ku.ulgen.entity.AffectedData;

@Repository
public interface AffectedDataRepository extends JpaRepository<AffectedData, String> {
}

