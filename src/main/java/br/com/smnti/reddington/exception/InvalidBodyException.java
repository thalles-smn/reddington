package br.com.smnti.reddington.exception;

public class InvalidBodyException extends BaseResourceException {
    private static final long serialVersionUID = -7712449305587063625L;

    public InvalidBodyException( String message ){
        super(message);
    }
}
