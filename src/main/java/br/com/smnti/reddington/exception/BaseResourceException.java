package br.com.smnti.reddington.exception;

public class BaseResourceException extends RuntimeException {
    private static final long serialVersionUID = 5357101752037554446L;

    public BaseResourceException( String message ){
        super(message);
    }
}
