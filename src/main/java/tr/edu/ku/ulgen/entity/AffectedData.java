package tr.edu.ku.ulgen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the affected cities data as an entity in the database.
 * Includes the city name and whether it is affected or not.
 *
 * @author Kaan Turkmen
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "affected_data")
public class AffectedData {

    @Id
    @Column(name = "city_name", nullable = false)
    private String cityName;

    @Column(name = "affected", nullable = false)
    private Boolean affected;
}
