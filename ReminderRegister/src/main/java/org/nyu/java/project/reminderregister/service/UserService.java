package org.nyu.java.project.reminderregister.service;

import org.nyu.java.project.reminderregister.dao.UserDao;
import org.nyu.java.project.reminderregister.entity.ERole;

import org.nyu.java.project.reminderregister.entity.User;
import org.nyu.java.project.reminderregister.model.request.EmailVerificationRequest;
import org.nyu.java.project.reminderregister.model.request.LoginRequest;
import org.nyu.java.project.reminderregister.model.request.SignupRequest;
import org.nyu.java.project.reminderregister.model.response.MessageResponse;
import org.nyu.java.project.reminderregister.security.jwt.JwtUtils;
import org.nyu.java.project.reminderregister.security.model.JwtResponse;
import org.nyu.java.project.reminderregister.security.model.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserDao userDao;


    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {


        try {
            User user = userDao.findUserByUsername(loginRequest.getUsername()).orElse(null);

            if (user == null || !user.isEmailVerified()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error: Account is not verified"));
            }

            UserDetailsImpl userDetails1 = UserDetailsImpl.build(user);
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword(),userDetails1.getAuthorities()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String jwt = jwtUtils.generateTokenFromUsername(userDetails.getUsername());
            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: Authentication failed"));
        } catch (SQLException sqlException) {
            String msg = String.format("Error: %s", sqlException.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(msg));
        }
    }

    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        try {
            if (userDao.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
            }

            if (userDao.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
            }

            User user = createUser(signUpRequest);
            userDao.insertUser(user);
            generateAndSendOTP(user);


            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (SQLException e) {
            String msg = String.format("Error: %s", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(msg));

        }

    }

    private User createUser(SignupRequest signUpRequest) {
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));
        user.setRoles(ERole.ROLE_USER.name());
//        ERole roles = processUserRoles(signUpRequest.getRole());
//        user.setRoles(roles.name());
        return user;
    }

    private ERole processUserRoles(String strRole) {


        if (strRole == null || strRole.isEmpty()) {
            return ERole.ROLE_USER;
        } else {

            switch (strRole) {
                case "admin":
                    return ERole.ROLE_ADMIN;


                default:
                    return ERole.ROLE_USER;
            }
        }
    }

//    private Role findRole(ERole roleName) {
//        return roleRepository.findByName(roleName)
//                .orElseThrow(() -> new RuntimeException("Error: Role " + roleName + " is not found."));
//    }

    public ResponseEntity<?> logoutUser() {
        return ResponseEntity.ok(new MessageResponse("You've been signed out!"));
    }

    public void generateAndSendOTP(User user) {
        String otp = generateOTP();
        user.setOtp(otp);
        user.setOtpRequestedTime(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        userDao.updateUser(user);
        sendOTPEmail(user.getEmail(), otp);
    }

    private String generateOTP() {
        Random random = new Random();
        int otpNumber = 100000 + random.nextInt(900000); // 6 digit OTP
        return String.valueOf(otpNumber);
    }

    public ResponseEntity<?> generateOtpAfterExpirationOrNewTry(EmailVerificationRequest emailVerificationRequest) {
        User user = null;
        try {
            user = userDao.findUserByEmail(emailVerificationRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found."));
            generateAndSendOTP(user);
            return ResponseEntity.ok(new MessageResponse("OTP generated successfully."));

        } catch (SQLException e) {

            String msg = String.format("Error: %s", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(msg));

        }

    }

    public ResponseEntity<?> validateOTP(EmailVerificationRequest emailVerificationRequest) {
        User user = null;
        try {
            user = userDao.findUserByEmail(emailVerificationRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found."));
            long otpTimeInEpocSec = user.getOtpRequestedTime();
            Instant instant = Instant.ofEpochSecond(otpTimeInEpocSec);
            ZoneId zoneId = ZoneId.of("UTC");
            LocalDateTime otpDateTime = instant.atZone(zoneId).toLocalDateTime();

            if (user.getOtp().trim().equals(emailVerificationRequest.getOtp()) && LocalDateTime.now().isBefore(otpDateTime.plusMinutes(5))) {
                user.setEmailVerified(true);
                userDao.updateUser(user);
                return ResponseEntity.ok(new MessageResponse("Email verified successfully."));
            } else if (user.getOtp().equals(emailVerificationRequest.getOtp()))
                return ResponseEntity.badRequest().body(new MessageResponse("OTP expired."));
            else {
                return ResponseEntity.badRequest().body(new MessageResponse("Invalid OTP"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    private void sendOTPEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP for Email Verification");
        message.setText("Dear User,\n\nYour OTP for email verification is: " + otp + "\n\nPlease use this OTP to complete your registration.\n\nThank you.");

        try {
            mailSender.send(message);
        } catch (MailException e) {

        }
    }
}
