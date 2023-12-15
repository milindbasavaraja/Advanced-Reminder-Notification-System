package com.nyu.java.reminderui.config;

public class AuthEndpoints {
    private String login;
    private String signup;

    private String verifyEmail;

    private String  generateOtp;

    public String getGenerateOtp() {
        return generateOtp;
    }

    public void setGenerateOtp(String generateOtp) {
        this.generateOtp = generateOtp;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSignup() {
        return signup;
    }

    public void setSignup(String signup) {
        this.signup = signup;
    }

    public String getVerifyEmail() {
        return verifyEmail;
    }

    public void setVerifyEmail(String verifyEmail) {
        this.verifyEmail = verifyEmail;
    }
}
