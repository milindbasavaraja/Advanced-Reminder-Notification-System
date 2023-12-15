package com.nyu.java.reminderui.model.response;

public class JwtValidationResponse {

   private boolean tokenValid;
   private String message;

   private UserInfoResponse userInfoResponse;


    public JwtValidationResponse() {
    }

    public JwtValidationResponse(boolean tokenValid, String message, UserInfoResponse userInfoResponse) {
        this.tokenValid = tokenValid;
        this.message = message;
        this.userInfoResponse = userInfoResponse;
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

    public void setTokenValid(boolean tokenValid) {
        this.tokenValid = tokenValid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
