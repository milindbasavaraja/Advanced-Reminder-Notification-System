package org.nyu.java.project.reminderregister.model.response;

public class MessageResponse {
    private String message;

    private boolean tokenValid;

    private UserInfoResponse userInfoResponse;


    public MessageResponse(String message, boolean tokenValid, UserInfoResponse userName) {
        this.message = message;
        this.tokenValid = tokenValid;
        this.userInfoResponse = userName;
    }

    public UserInfoResponse getUserInfoResponse() {
        return userInfoResponse;
    }

    public void setUserInfoResponse(UserInfoResponse userInfoResponse) {
        this.userInfoResponse = userInfoResponse;
    }

    public boolean isTokenValid() {
        return tokenValid;
    }

    public void setTokenValid(boolean isTokenValid) {
        this.tokenValid = isTokenValid;
    }


    public MessageResponse(boolean isTokenValid) {
        this.tokenValid = isTokenValid;
    }

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
