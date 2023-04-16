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
 * Represents the MAC address entity in the database.
 * Includes the manufacturer part of the MAC address.
 *
 * @author Kaan Turkmen
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mac_address")
public class MACAddress {
    @Id
    @Column(name = "manufacturer_part")
    private String manufacturer;
}
