package com.junk.application.discordfeedhub.utils;

/**
 *
 * @author elmerhd
 */
public class DatabaseStatementStatus {
    
    private boolean success = false;
    private String statusMessage = null;

    public DatabaseStatementStatus(boolean isSuccess, String statusMessage) {
        this.success = isSuccess;
        this.statusMessage = statusMessage;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public boolean isSuccess() {
        return success;
    }
}
