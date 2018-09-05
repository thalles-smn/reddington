package br.com.smnti.reddington.business;

import br.com.smnti.reddington.model.Address;

import java.util.List;
import java.util.Optional;

public interface AddressBusiness {

    Optional<Address> findByZipCode(String zipCode );
    Optional<Address> save( Address address );
    Optional<List<Address>> findAll();
    Optional<Address> findById( Long id );
    void delete( Long id );

}
