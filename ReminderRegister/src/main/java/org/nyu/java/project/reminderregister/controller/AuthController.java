package org.nyu.java.project.reminderregister.controller;


import org.nyu.java.project.reminderregister.model.request.EmailVerificationRequest;
import org.nyu.java.project.reminderregister.model.request.LoginRequest;
import org.nyu.java.project.reminderregister.model.request.SignupRequest;
import org.nyu.java.project.reminderregister.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return userService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return userService.registerUser(signUpRequest);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        return userService.logoutUser();
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> registerUser(@Valid @RequestBody EmailVerificationRequest emailVerificationRequest) {
        return userService.validateOTP(emailVerificationRequest);
    }

    @PostMapping("/generate-otp")
    public ResponseEntity<?> generateOtp(@Valid @RequestBody EmailVerificationRequest emailVerificationRequest) {
        return userService.generateOtpAfterExpirationOrNewTry(emailVerificationRequest);
    }


}
