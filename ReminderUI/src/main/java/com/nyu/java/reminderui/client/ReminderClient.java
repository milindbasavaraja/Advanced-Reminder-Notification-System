package com.nyu.java.reminderui.client;

import com.nyu.java.reminderui.config.PropertiesConfig;
import com.nyu.java.reminderui.config.TokenStorage;
import com.nyu.java.reminderui.exceptions.*;
import com.nyu.java.reminderui.model.request.ReminderCreationRequest;
import com.nyu.java.reminderui.model.response.JwtValidationResponse;
import com.nyu.java.reminderui.model.response.Reminder;
import com.nyu.java.reminderui.model.response.UserInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.util.List;

@Service
public class ReminderClient {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticateClient.class);
    private final PropertiesConfig propertiesConfig;
    private final RestTemplate restTemplate;

    public ReminderClient(PropertiesConfig propertiesConfig, RestTemplate restTemplate) {
        this.propertiesConfig = propertiesConfig;
        this.restTemplate = restTemplate;
    }


    public UserInfoResponse retrieveUserNameFromJwtToken(String jwtToken) throws UserNotFoundException {
        String url = propertiesConfig.getBaseEndpoint() + "/" + propertiesConfig.getTest().getRetrieveUsername();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<JwtValidationResponse> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, JwtValidationResponse.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getUserInfoResponse();
            } else {
                throw new UserNotFoundException("Username not found in JWT token");
            }
        } catch (HttpStatusCodeException e) {
            throw new UserNotFoundException("Error retrieving username: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new UserNotFoundException("Error retrieving username: " + e.getMessage());
        }
    }

    public void createNewReminder(ReminderCreationRequest reminderCreationRequest) throws ReminderCreationException {
        String url = propertiesConfig.getBaseEndpoint() + "/" + propertiesConfig.getTest().getCreateReminder();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + TokenStorage.getToken());

        HttpEntity<ReminderCreationRequest> request = new HttpEntity<>(reminderCreationRequest, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ReminderCreationException("Error while creating reminder: " + response.getBody());
            }
        } catch (RestClientException e) {
            throw new ReminderCreationException("REST client exception: " + e.getMessage());
        } catch (Exception e) {
            throw new ReminderCreationException("General exception: " + e.getMessage());
        }
    }

    public List<Reminder> getRemindersForUser() throws NoRemindersFoundException {
        String url = propertiesConfig.getBaseEndpoint() + "/" + propertiesConfig.getTest().getRetrieveReminder();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TokenStorage.getToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ParameterizedTypeReference<List<Reminder>> typeRef = new ParameterizedTypeReference<List<Reminder>>() {
            };
            ResponseEntity<List<Reminder>> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, typeRef);

            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                List<Reminder> reminders = responseEntity.getBody();
                return reminders;
            } else {
                throw new NoRemindersFoundException("No reminders found");
            }
        } catch (HttpStatusCodeException e) {
            throw new NoRemindersFoundException("Error retrieving reminders: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new NoRemindersFoundException("Error retrieving reminders: " + e.getMessage());
        }
    }

    public void updateReminder(Reminder reminder) throws ReminderUpdateException {
        String url = propertiesConfig.getBaseEndpoint() + "/" + propertiesConfig.getTest().getUpdateReminder();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + TokenStorage.getToken());

        HttpEntity<Reminder> request = new HttpEntity<>(reminder, headers);

        try {

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    request,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ReminderUpdateException("Error while updating reminder: " + response.getBody());
            }
        } catch (RestClientException e) {
            throw new ReminderUpdateException("REST client exception: " + e.getMessage());
        } catch (Exception e) {
            throw new ReminderUpdateException("General exception: " + e.getMessage());
        }
    }

    public void deleteReminder(Integer id) throws RestClientException, ReminderDeleteException {
        String url = propertiesConfig.getBaseEndpoint() + "/" + propertiesConfig.getTest().getDeleteReminder() + "/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + TokenStorage.getToken());
        HttpEntity<?> request = new HttpEntity<>(headers);
        logger.info("THe url is:{}", url);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    request,
                    String.class
            );

        } catch (HttpClientErrorException e) {
            throw new ReminderDeleteException("Client error: " + e.getMessage());
        } catch (HttpServerErrorException e) {
            throw new ReminderDeleteException("Server error: " + e.getMessage());
        } catch (Exception e) {
            throw new ReminderDeleteException("General error: " + e.getMessage());
        }
    }
}
