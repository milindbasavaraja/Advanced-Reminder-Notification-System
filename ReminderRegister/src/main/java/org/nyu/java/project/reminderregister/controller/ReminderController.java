package org.nyu.java.project.reminderregister.controller;

import org.nyu.java.project.reminderregister.entity.ReminderEntity;
import org.nyu.java.project.reminderregister.entity.User;
import org.nyu.java.project.reminderregister.model.request.ReminderCreationRequest;
import org.nyu.java.project.reminderregister.model.request.ReminderUpdateRequest;
import org.nyu.java.project.reminderregister.model.response.MessageResponse;
import org.nyu.java.project.reminderregister.model.response.ReminderResponse;
import org.nyu.java.project.reminderregister.model.response.UserInfoResponse;
import org.nyu.java.project.reminderregister.repository.ReminderRepository;
import org.nyu.java.project.reminderregister.repository.UserRepository;
import org.nyu.java.project.reminderregister.security.jwt.JwtUtils;
import org.nyu.java.project.reminderregister.service.ReminderPollingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test")
public class ReminderController {

    private Logger logger = LoggerFactory.getLogger(ReminderController.class);

    @Autowired
    private ReminderPollingService reminderPollingService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    @GetMapping("/validate-jwt")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> validateJwT(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new MessageResponse(false));
            }
            String token = authHeader.substring(7);
            boolean isValid = jwtUtils.validateJwtToken(token);
            String userName = null;
            if (isValid) {
                userName = jwtUtils.getUserNameFromJwtToken(token);
            }

            User user = userRepository.findByUsername(userName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            UserInfoResponse userInfoResponse = new UserInfoResponse(user.getId(),user.getUsername(),user.getEmail(), Collections.EMPTY_LIST);
            return ResponseEntity.ok(new MessageResponse("User Logged In", isValid,userInfoResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse(false));
        }
    }

    @GetMapping("/retrieve-username")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> retreiveUserName(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new MessageResponse(false));
            }
            String token = authHeader.substring(7);
            boolean isValid = jwtUtils.validateJwtToken(token);

            String userName = null;
            if (isValid) {
                userName = jwtUtils.getUserNameFromJwtToken(token);
            }

            User user = userRepository.findByUsername(userName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            UserInfoResponse userInfoResponse = new UserInfoResponse(user.getId(),user.getUsername(),user.getEmail(), Collections.EMPTY_LIST);

            return ResponseEntity.ok(new MessageResponse("User Logged In", isValid, userInfoResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse(false));
        }
    }

    @PostMapping("/create-reminder")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> createReminder(@RequestBody ReminderCreationRequest reminderCreationRequest) {
        try {

            User user = userRepository.findByUsername(reminderCreationRequest.getUserName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));


            ReminderEntity reminderEntity = new ReminderEntity(reminderCreationRequest.getReminderName(),
                    reminderCreationRequest.getReminderWay(),
                    reminderCreationRequest.getReminderDate(),
                    reminderCreationRequest.getReminderTime(),
                    reminderCreationRequest.getReminderDescription(),
                    reminderCreationRequest.getReminderToEvent());
            reminderEntity.setUser(user);
            reminderRepository.save(reminderEntity);
            reminderPollingService.eventTriggeredPollExpiringReminders();
            return ResponseEntity.ok(new MessageResponse("Reminder created successfully"));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(e.getMessage()));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Data integrity violation: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error: Unable to create reminder - " + e.getMessage()));
        }

    }

    @GetMapping("/retrieve-reminder")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserSpecificReminder(@RequestHeader("Authorization") String authHeader) {
        try {

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new MessageResponse(false));
            }
            String token = authHeader.substring(7);
            boolean isValid = jwtUtils.validateJwtToken(token);

            String userName = null;
            if (isValid) {
                userName = jwtUtils.getUserNameFromJwtToken(token);
            }


            User user = userRepository.findByUsername(userName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));


            List<ReminderEntity> reminderEntityList = reminderRepository.findByUserId(user.getId());
            List<ReminderEntity> activeReminders = reminderEntityList.stream().filter(reminder-> !reminder.getExpired()).collect(Collectors.toList());
            List<ReminderResponse> reminderResponses = activeReminders.stream().map(ReminderResponse::new).collect(Collectors.toList());

            return ResponseEntity.ok(reminderResponses);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(e.getMessage()));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse("Data integrity violation: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error: Unable to create reminder - " + e.getMessage()));
        }

    }

    @PutMapping("/update-reminder")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateReminder(@RequestBody ReminderUpdateRequest reminderRequest) {
        try {
            logger.info("Updating Reminder:{}", reminderRequest);

            ReminderEntity reminder = reminderRepository.findById(reminderRequest.getReminderId())
                    .orElseThrow(() -> new EntityNotFoundException("Reminder not found with id: " + reminderRequest.getReminderId()));
            logger.info("Reminder Retrived:{}", reminder);
            User user = userRepository.findByUsername(reminderRequest.getUserName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            reminder.setReminderName(reminderRequest.getReminderName());
            reminder.setReminderWay(reminderRequest.getReminderWay());
            reminder.setReminderDate(reminderRequest.getReminderDate());
            reminder.setReminderTime(reminderRequest.getReminderTime());
            reminder.setReminderDescription(reminderRequest.getReminderDescription());
            reminder.setReminderToEvent(reminderRequest.getReminderToEvent());
            reminder.setUser(user);
            reminderRepository.save(reminder);
            logger.info("Refreshing next 5 min reminders");
            reminderPollingService.eventTriggeredPollExpiringReminders();
            return ResponseEntity.ok(new MessageResponse("Reminder updated successfully"));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(e.getMessage()));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new MessageResponse("Data integrity violation: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error: Unable to update reminder - " + e.getMessage()));
        }
    }


    @DeleteMapping("/delete-reminder/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteReminder(@PathVariable Integer id) {
        try {
            logger.info("Deletingid:{}",id);
            ReminderEntity reminder = reminderRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Reminder not found with id: " + id));
            reminderRepository.delete(reminder);
            reminderPollingService.eventTriggeredPollExpiringReminders();
            return ResponseEntity.ok(new MessageResponse("Reminder deleted successfully"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Reminder not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error occurred: " + e.getMessage()));
        }
    }

}
