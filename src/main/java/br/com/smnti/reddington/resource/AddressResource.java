package br.com.smnti.reddington.resource;

import br.com.smnti.reddington.business.AddressBusiness;
import br.com.smnti.reddington.model.Address;
import br.com.smnti.reddington.resource.common.BaseResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1")
@Slf4j
public class AddressResource extends BaseResource {

    @Autowired
    private AddressBusiness addressBusiness;

    @GetMapping
    @ResponseBody
    public ResponseEntity<?> index() {
        return this.buildResponse(HttpStatus.OK, this.addressBusiness.findAll());
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody @Valid Address address, Errors errors){
        this.validate(errors);

        return this.buildResponse(HttpStatus.CREATED, this.addressBusiness.save(address));
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> findById( @PathVariable("id") Long id) {
        return this.buildResponse(HttpStatus.OK, this.addressBusiness.findById(id));
    }

    @GetMapping("/zipCode/{zipCode}")
    @ResponseBody
    public ResponseEntity<?> findByZipCode( @PathVariable("zipCode") String zipCode) {
        return this.buildResponse(HttpStatus.OK, this.addressBusiness.findByZipCode(zipCode));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete( @PathVariable("id") Long id ){
        this.addressBusiness.delete(id);
    }

}
