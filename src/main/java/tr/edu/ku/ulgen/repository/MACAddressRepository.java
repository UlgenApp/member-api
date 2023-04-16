package tr.edu.ku.ulgen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tr.edu.ku.ulgen.entity.MACAddress;

import java.util.Collection;
import java.util.List;

/**
 * Repository interface for performing CRUD operations on the MACAddress entity.
 * Includes a custom method to find all MACAddresses with manufacturers in a given collection.
 *
 * @author Kaan Turkmen
 */
@Repository
public interface MACAddressRepository extends JpaRepository<MACAddress, String> {

    List<MACAddress> findAllByManufacturerIn(Collection<String> macAddressList);
}
