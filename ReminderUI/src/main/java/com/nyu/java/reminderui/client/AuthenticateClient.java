package com.nyu.java.reminderui.client;

import com.nyu.java.reminderui.config.PropertiesConfig;
import com.nyu.java.reminderui.exceptions.AuthenticationException;
import com.nyu.java.reminderui.exceptions.EmailNotVerifiedException;
import com.nyu.java.reminderui.model.request.EmailVerificationRequest;
import com.nyu.java.reminderui.model.request.LoginRequest;
import com.nyu.java.reminderui.model.request.SignUpRequest;
import com.nyu.java.reminderui.model.response.JwtValidationResponse;
import com.nyu.java.reminderui.model.response.LogInResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthenticateClient {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticateClient.class);
    private final PropertiesConfig propertiesConfig;
    private final RestTemplate restTemplate;


    public AuthenticateClient(PropertiesConfig propertiesConfig, RestTemplate restTemplate) {
        this.propertiesConfig = propertiesConfig;
        this.restTemplate = restTemplate;
    }

    public String login(LoginRequest loginRequest) throws AuthenticationException, EmailNotVerifiedException {
        String url = propertiesConfig.getBaseEndpoint() + "/" + propertiesConfig.getAuth().getLogin();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);
        ResponseEntity<LogInResponse> response = null;
        try {
            response = restTemplate.postForEntity(url, request, LogInResponse.class);
            if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {

                LogInResponse logInResponse = response.getBody();
                if (logInResponse == null || logInResponse.getToken() == null || logInResponse.getToken().isEmpty()) {
                    String errorMsg = String.format("The response retrieved from server is null or token is null: [%s]", logInResponse);
                    logger.error(errorMsg);
                    throw new AuthenticationException(errorMsg);
                }
                logger.info("Login Successful: {}", logInResponse.getToken());
                return logInResponse.getToken();
            } else {
                logger.error("Error while logging in: status-code:{} error-msg: {}", response.getStatusCode(), response.getBody());
                throw new AuthenticationException("Failed to retrieve JWT token");
            }
        } catch (Exception e) {
            if (e.getMessage().contains("Error: Account is not verified")) {
                throw new EmailNotVerifiedException(e.getMessage());
            }
            String msg = String.format("Error while logging in with errorMsg: {%s}", e.getMessage());
            logger.error(msg);
            throw new AuthenticationException(msg);
        }

    }

    public boolean signUp(SignUpRequest signUpRequest) throws AuthenticationException {
        String url = propertiesConfig.getBaseEndpoint() + "/" + propertiesConfig.getAuth().getSignup();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SignUpRequest> request = new HttpEntity<>(signUpRequest, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("SignUp Successful");
                return true;
            } else {
                logger.error("Error while signing up: status-code:{} error-msg: {}", response.getStatusCode(), response.getBody());
                throw new AuthenticationException("Error during signup: " + response.getBody());
            }
        } catch (HttpStatusCodeException e) {
            logger.error("Error while signing up: status-code:{} error-msg: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new AuthenticationException("Signup failed: " + e.getResponseBodyAsString());
        }
    }

    public JwtValidationResponse validateJwT(String jwtToken) {
        String url = propertiesConfig.getBaseEndpoint() + "/" + propertiesConfig.getTest().getValidateJwt();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<JwtValidationResponse> response;
        try {
            response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, JwtValidationResponse.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                logger.info("Valid JwT");

            }
        } catch (HttpStatusCodeException e) {
            logger.error("Error while signing up: status-code:{} error-msg: {}", e.getStatusCode(), e.getResponseBodyAsString());
            return null;
        }
        return response.getBody();

    }

    public void verifyEmail(String email, String otp) throws AuthenticationException {
        String url = propertiesConfig.getBaseEndpoint() + "/" + propertiesConfig.getAuth().getVerifyEmail();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        EmailVerificationRequest emailVerificationRequest = new EmailVerificationRequest(email, otp);
        HttpEntity<EmailVerificationRequest> request = new HttpEntity<>(emailVerificationRequest, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Email verification successful");
            } else {
                logger.error("Error in email verification: status-code:{} error-msg: {}", response.getStatusCode(), response.getBody());
                throw new AuthenticationException("Email verification failed: " + response.getBody());
            }
        } catch (HttpStatusCodeException e) {
            logger.error("Error in email verification: status-code:{} error-msg: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new AuthenticationException("Email verification error: " + e.getResponseBodyAsString());
        }
    }

    public void generateOtpAgain(String email) {
        String url = propertiesConfig.getBaseEndpoint() + "/" + propertiesConfig.getAuth().getGenerateOtp();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        EmailVerificationRequest emailVerificationRequest = new EmailVerificationRequest(email, "no-otp");
        HttpEntity<EmailVerificationRequest> request = new HttpEntity<>(emailVerificationRequest, headers);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("OTP generation successful");
            }

        } catch (HttpStatusCodeException e) {
            logger.error("Error in OTP generation: status-code:{} error-msg: {}", e.getStatusCode(), e.getResponseBodyAsString());

        }
    }


}
