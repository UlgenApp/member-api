package tr.edu.ku.ulgen.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tr.edu.ku.ulgen.entity.MACAddress;
import tr.edu.ku.ulgen.repository.MACAddressRepository;

import java.util.List;

/**
 * A service class provides service-level operations related to MACAddress entities.
 * The main responsibility of this class is to interact with the MACAddressRepository
 * and perform business logic on the retrieved MACAddress entities.
 *
 * @author Kaan Turkmen
 */
@Service
@AllArgsConstructor
public class MACAddressService {
    private final MACAddressRepository macAddressRepository;

    /**
     * Counts the number of MAC addresses in the data storage that match the given list of MAC addresses.
     *
     * @param macAddresses A list of MAC addresses to be matched against the data storage.
     * @return The number of matching MAC addresses found in the data storage.
     */
    public int countMatchingMACAddresses(List<String> macAddresses) {
        List<MACAddress> matchingAddresses = macAddressRepository.findAllByManufacturerIn(macAddresses);
        return matchingAddresses.size();
    }
}

