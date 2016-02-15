package org.outofrange.crowdsupport.util;

/**
 * An unchecked exception which can be thrown in the service layer to indicate a problem.
 */
public class ServiceException extends RuntimeException {
    /**
     * Creates a new {@code ServiceException} with a given message.
     *
     * @param message the message for the Exception
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Creates a new {@code ServiceException} with a given message and a cause.
     *
     * @param message the message for the Exception
     * @param cause   the cause for the exception
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
