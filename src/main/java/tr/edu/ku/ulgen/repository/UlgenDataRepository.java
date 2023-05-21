package tr.edu.ku.ulgen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.edu.ku.ulgen.entity.UlgenData;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on the UlgenData entity.
 * Includes a custom method to find all UlgenData with user cities in a given list.
 *
 * @author Kaan Turkmen
 */
public interface UlgenDataRepository extends JpaRepository<UlgenData, Integer> {

    List<UlgenData> findByUserCityIn(List<String> cities);

    Optional<UlgenData> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
