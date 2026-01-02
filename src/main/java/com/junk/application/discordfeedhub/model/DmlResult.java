package com.junk.application.discordfeedhub.model;

import com.junk.application.discordfeedhub.utils.DmlStatus;

/**
 *
 * @author elmerhd
 */
public record DmlResult (
        DmlStatus status,
        String message,
        Throwable throwable){
    
    public boolean isSuccess() {
        return status == DmlStatus.SUCCESS;
    }

    public static DmlResult success(int rowsAffected) {
        return new DmlResult(
                DmlStatus.SUCCESS,
                rowsAffected + " row(s) affected",
                null
        );
    }

    public static DmlResult failure(DmlStatus status, String message, Throwable t) {
        return new DmlResult(status, message, t);
    }
}
