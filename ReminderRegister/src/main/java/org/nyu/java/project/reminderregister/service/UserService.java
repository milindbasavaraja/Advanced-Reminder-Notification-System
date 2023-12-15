package org.nyu.java.project.reminderregister.service;

import org.nyu.java.project.reminderregister.entity.ERole;
import org.nyu.java.project.reminderregister.entity.Role;
import org.nyu.java.project.reminderregister.entity.User;
import org.nyu.java.project.reminderregister.model.request.EmailVerificationRequest;
import org.nyu.java.project.reminderregister.model.request.LoginRequest;
import org.nyu.java.project.reminderregister.model.request.SignupRequest;
import org.nyu.java.project.reminderregister.model.response.MessageResponse;
import org.nyu.java.project.reminderregister.repository.RoleRepository;
import org.nyu.java.project.reminderregister.repository.UserRepository;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JavaMailSender mailSender;


    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElse(null);

        if (user == null || !user.isEmailVerified()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error: Account is not verified"));
        }

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String jwt = jwtUtils.generateTokenFromUsername(userDetails.getUsername());
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: Authentication failed"));
        }
    }

    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = createUser(signUpRequest);
        generateAndSendOTP(user);
        userRepository.save(user);


        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    private User createUser(SignupRequest signUpRequest) {
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));
        Set<Role> roles = processUserRoles(signUpRequest.getRole());
        user.setRoles(roles);
        return user;
    }

    private Set<Role> processUserRoles(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        roles.add(findRole(ERole.ROLE_ADMIN));
                        break;
                    default:
                        roles.add(findRole(ERole.ROLE_USER));
                }
            });
        }

        return roles;
    }

    private Role findRole(ERole roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Error: Role " + roleName + " is not found."));
    }

    public ResponseEntity<?> logoutUser() {
        return ResponseEntity.ok(new MessageResponse("You've been signed out!"));
    }

    public void generateAndSendOTP(User user) {
        String otp = generateOTP();
        user.setOtp(otp);
        user.setOtpRequestedTime(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        userRepository.save(user);
        sendOTPEmail(user.getEmail(), otp);
    }

    private String generateOTP() {
        Random random = new Random();
        int otpNumber = 100000 + random.nextInt(900000); // 6 digit OTP
        return String.valueOf(otpNumber);
    }

    public ResponseEntity<?> generateOtpAfterExpirationOrNewTry(EmailVerificationRequest emailVerificationRequest) {
        User user = userRepository.findByEmail(emailVerificationRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found."));
        generateAndSendOTP(user);
        return ResponseEntity.ok(new MessageResponse("OTP generated successfully."));

    }

    public ResponseEntity<?> validateOTP(EmailVerificationRequest emailVerificationRequest) {
        User user = userRepository.findByEmail(emailVerificationRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found."));

        long otpTimeInEpocSec = user.getOtpRequestedTime();
        Instant instant = Instant.ofEpochSecond(otpTimeInEpocSec);
        ZoneId zoneId = ZoneId.of("UTC");
        LocalDateTime otpDateTime = instant.atZone(zoneId).toLocalDateTime();

        if (user.getOtp().trim().equals(emailVerificationRequest.getOtp())
                &&
                LocalDateTime.now().isBefore(otpDateTime.plusMinutes(5))) {
            user.setEmailVerified(true);
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("Email verified successfully."));
        } else if (user.getOtp().equals(emailVerificationRequest.getOtp()))
            return ResponseEntity.badRequest().body(new MessageResponse("OTP expired."));
        else {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid OTP"));
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
