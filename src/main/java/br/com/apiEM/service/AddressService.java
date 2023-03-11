package br.com.apiEM.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.apiEM.model.Address;
import br.com.apiEM.repository.AddressRepository;
import jakarta.mail.internet.AddressException;
import jakarta.transaction.Transactional;

@Service
public class AddressService {
  

  @Autowired
  private AddressRepository addressRepository;


  @Transactional
  public Address register(Address address) {

    address.setIsActive(true);

    return addressRepository.save(address);

  }


  @Transactional
  public Address update(Address address, Long id) throws AddressException {

    Address a = addressRepository.findById(id).orElseThrow(() -> new AddressException("not find id, " + id));


    if(address.getCep() != null) {

      a.setCep(address.getCep());
        
    }

    if(address.getCity() != null) {
      
      a.setCity(address.getCity());

    }

    if(address.getComplement() != null) {
      
      a.setComplement(address.getComplement());
    }

    if(address.getNeighborhood() != null) {

      a.setNeighborhood(address.getNeighborhood());
    }

    if(address.getNumber() != null) {

      a.setNumber(address.getNumber());
    }

    if(address.getStreet() != null) {

      a.setStreet(address.getStreet());
    }

    if(address.getUf() != null) {

      a.setUf(address.getUf());

    }

    return addressRepository.save(a);
  }

  @Transactional
  public void delete(Long id) throws AddressException {

    Address a = addressRepository.findById(id)
    .orElseThrow(() -> new AddressException("not find id,  " + id));

    a.setIsActive(false);

  }
  

}
