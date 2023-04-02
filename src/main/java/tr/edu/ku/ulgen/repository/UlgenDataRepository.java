package tr.edu.ku.ulgen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tr.edu.ku.ulgen.entity.UlgenData;

import java.util.List;


public interface UlgenDataRepository extends JpaRepository<UlgenData, Integer> {

    List<UlgenData> findByUserCityIn(List<String> cities);
}
