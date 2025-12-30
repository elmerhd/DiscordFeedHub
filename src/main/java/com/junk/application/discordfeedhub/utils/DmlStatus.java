package com.junk.application.discordfeedhub.utils;

/**
 *
 * @author elmerhd
 */
public enum DmlStatus {
    SUCCESS,
    NO_ROWS_AFFECTED,
    CONSTRAINT_VIOLATION,
    DUPLICATE_KEY,
    INVALID_QUERY,
    TRANSACTION_FAILED,
    CONNECTION_ERROR,
    PERMISSION_DENIED,
    TIMEOUT,
    UNKNOWN_ERROR
}
