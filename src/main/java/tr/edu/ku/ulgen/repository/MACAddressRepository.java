package tr.edu.ku.ulgen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tr.edu.ku.ulgen.entity.MACAddress;

import java.util.Collection;
import java.util.List;

@Repository
public interface MACAddressRepository extends JpaRepository<MACAddress, String> {

    List<MACAddress> findAllByManufacturerIn(Collection<String> macAddressList);
}
