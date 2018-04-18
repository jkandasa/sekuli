package com.redhat.qe.sekuli.common.exceptions;

/**
 * @author Jeeva Kandasamy (jkandasa)
 */

public class MissingMandatoryFieldsException extends RuntimeException {

    /**  */
    private static final long serialVersionUID = 1417810609365995570L;

    public MissingMandatoryFieldsException(String message) {
        super(message);
    }
}
