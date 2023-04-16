package tr.edu.ku.ulgen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tr.edu.ku.ulgen.entity.UlgenConfig;

/**
 * Repository interface for performing CRUD operations on the UlgenConfig entity.
 *
 * @author Kaan Turkmen
 */
@Repository
public interface UlgenConfigRepository extends JpaRepository<UlgenConfig, String> {
}