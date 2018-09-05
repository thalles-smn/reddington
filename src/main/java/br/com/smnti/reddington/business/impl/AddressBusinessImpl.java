package br.com.smnti.reddington.business.impl;

import br.com.smnti.reddington.business.AddressBusiness;
import br.com.smnti.reddington.exception.ZipCodeAlreadyExists;
import br.com.smnti.reddington.model.Address;
import br.com.smnti.reddington.repository.AddressRepository;
import br.com.smnti.reddington.service.viacep.ViaCepResponse;
import br.com.smnti.reddington.service.viacep.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressBusinessImpl implements AddressBusiness {

    private AddressRepository addressRepository;
    private ViaCepService viaCepService;

    @Autowired
    public AddressBusinessImpl( AddressRepository addressRepository, ViaCepService viaCepService ){
        this.addressRepository = addressRepository;
        this.viaCepService = viaCepService;
    }

    @Override
    public Optional<Address> findByZipCode(String zipCode) {

        Optional<Address> opAddress = Optional.ofNullable(this.addressRepository.findByZipCode(zipCode));

        if(!opAddress.isPresent()){
            Optional<ViaCepResponse> response = this.viaCepService.findAddress(zipCode);

            if(response.isPresent()){
                return Optional.of(this.addressRepository.save(this.toAddress(response.get())));
            }

        }
        else {
            Address address = opAddress.get();
            address.setRetentative(address.getRetentative() + 1);
            return Optional.ofNullable(this.addressRepository.save(address));
        }

        return Optional.empty();
    }

    @Override
    public Optional<Address> save(Address address) {

        if(Optional.ofNullable(this.addressRepository.findByZipCode(address.getZipCode())).isPresent()){
            throw new ZipCodeAlreadyExists();
        }

        return Optional.ofNullable(this.addressRepository.save(address));
    }

    @Override
    public Optional<List<Address>> findAll() {
        return Optional.ofNullable(this.addressRepository.findAll());
    }

    @Override
    public Optional<Address> findById(Long id) {
        return this.addressRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        this.addressRepository.findById(id).ifPresent(address -> this.addressRepository.delete(address));
    }

    private Address toAddress( ViaCepResponse viaCepResponse ){

        return Address.builder()
                .street(viaCepResponse.getStreet())
                .district(viaCepResponse.getDistrict())
                .city(viaCepResponse.getCity())
                .state(viaCepResponse.getState())
                .zipCode(viaCepResponse.getZipCode().replace("-", ""))
                .retentative(0)
                .build();

    }
}
