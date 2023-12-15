package com.nyu.java.reminderui.config;

public class TestEndpoints {
    private String basicUrl;

    private String retrieveUsername;
    private String validateJwt;

    private String createReminder;

    private String retrieveReminder;

    private String updateReminder;

    private String deleteReminder;

    public String getDeleteReminder() {
        return deleteReminder;
    }

    public void setDeleteReminder(String deleteReminder) {
        this.deleteReminder = deleteReminder;
    }

    public String getUpdateReminder() {
        return updateReminder;
    }

    public void setUpdateReminder(String updateReminder) {
        this.updateReminder = updateReminder;
    }

    public String getRetrieveReminder() {
        return retrieveReminder;
    }

    public void setRetrieveReminder(String retrieveReminder) {
        this.retrieveReminder = retrieveReminder;
    }

    public String getCreateReminder() {
        return createReminder;
    }

    public void setCreateReminder(String createReminder) {
        this.createReminder = createReminder;
    }

    public String getRetrieveUsername() {
        return retrieveUsername;
    }

    public void setRetrieveUsername(String retrieveUsername) {
        this.retrieveUsername = retrieveUsername;
    }

    public String getValidateJwt() {
        return validateJwt;
    }

    public void setValidateJwt(String validateJwt) {
        this.validateJwt = validateJwt;
    }

    public String getBasicUrl() {
        return basicUrl;
    }

    public void setBasicUrl(String basicUrl) {
        this.basicUrl = basicUrl;
    }
}
