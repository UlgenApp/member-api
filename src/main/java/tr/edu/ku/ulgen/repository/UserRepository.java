package tr.edu.ku.ulgen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import tr.edu.ku.ulgen.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.additionalInfo = ?1 WHERE u.id = ?2")
    Integer updateAdditionalInfo(String additionalInfo, Long userId);
}
