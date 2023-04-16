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
 * Represents the Ulgen configuration entity in the database.
 * Includes the configuration property name and value.
 *
 * @author Kaan Turkmen
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "config")
public class UlgenConfig {

    @Id
    @Column(name = "config_property_name", nullable = false)
    private String configPropertyName;

    @Column(name = "config_property_value", nullable = false)
    private Boolean configPropertyValue;
}
