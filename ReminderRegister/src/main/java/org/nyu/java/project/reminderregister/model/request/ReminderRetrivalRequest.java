package org.nyu.java.project.reminderregister.model.request;

public class ReminderRetrivalRequest {
    private  String userName;



    public ReminderRetrivalRequest(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
