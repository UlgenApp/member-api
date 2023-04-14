package tr.edu.ku.ulgen.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tr.edu.ku.ulgen.entity.MACAddress;
import tr.edu.ku.ulgen.repository.MACAddressRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class MACAddressService {
    private final MACAddressRepository macAddressRepository;

    public int countMatchingMACAddresses(List<String> macAddresses) {
        List<MACAddress> matchingAddresses = macAddressRepository.findAllByManufacturerIn(macAddresses);
        return matchingAddresses.size();
    }
}

