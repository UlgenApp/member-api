package tr.edu.ku.ulgen.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the Ulgen data entity in the database.
 * Includes user ID, number of active users, user location, and user city.
 *
 * @author Kaan Turkmen
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ulgen-data")
public class UlgenData {

    @Id
    private Long userId;
    private Integer activeUsers;
    private Double latitude;
    private Double longitude;
    private String userCity;
}
