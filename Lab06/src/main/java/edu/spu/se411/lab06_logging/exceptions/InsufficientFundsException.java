package edu.spu.se411.lab06_logging.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsufficientFundsException extends Exception {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(InsufficientFundsException.class);

    public InsufficientFundsException(String message) {
        super(message);
        // Required by Lab 06; log the stack trace once, at the handling boundary.
        logger.warn("InsufficientFundsException object created");
    }
}
