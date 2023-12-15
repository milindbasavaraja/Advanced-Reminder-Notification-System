package org.nyu.java.project.reminderregister.entity;


import java.util.Objects;

public class User {

    private Long id;


    private String username;


    private String email;


    private String password;

    private String otp;
    private Long otpRequestedTime;
    private boolean isEmailVerified;

    private String role;


    public User() {
    }


    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.isEmailVerified = false;

    }

    public User(String username, String email, String password, String otp, Long otpRequestedTime, boolean isEmailVerified, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.otp = otp;
        this.otpRequestedTime = otpRequestedTime;
        this.isEmailVerified = isEmailVerified;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return role;
    }

    public void setRoles(String roles) {
        this.role = roles;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Long getOtpRequestedTime() {
        return otpRequestedTime;
    }

    public void setOtpRequestedTime(Long otpRequestedTime) {
        this.otpRequestedTime = otpRequestedTime;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", otp='" + otp + '\'' +
                ", otpRequestedTime=" + otpRequestedTime +
                ", isEmailVerified=" + isEmailVerified +
                ", roles=" + role +
                '}';
    }
}
