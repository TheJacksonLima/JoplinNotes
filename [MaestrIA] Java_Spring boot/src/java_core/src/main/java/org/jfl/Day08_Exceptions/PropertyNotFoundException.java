package org.jfl.Day08_Exceptions;

public class PropertyNotFoundException extends Exception{

    public PropertyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyNotFoundException(String message) {
        super(message);
    }

    public PropertyNotFoundException(Long id) {
        super("Property no found, id:"+id);
    }

    public PropertyNotFoundException() {
    }
}
