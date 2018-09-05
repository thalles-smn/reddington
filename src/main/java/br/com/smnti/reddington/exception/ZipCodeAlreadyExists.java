package br.com.smnti.reddington.exception;

public class ZipCodeAlreadyExists extends BaseResourceException {

    private static final long serialVersionUID = 2804609635843608648L;

    public ZipCodeAlreadyExists() {
        super("zipCode already exists");
    }
}
