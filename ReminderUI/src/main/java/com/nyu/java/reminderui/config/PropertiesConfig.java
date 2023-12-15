package com.nyu.java.reminderui.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "endpoints")
public class PropertiesConfig {
    private String baseEndpoint;
    private AuthEndpoints auth;
    private TestEndpoints test;

    public String getBaseEndpoint() {
        return baseEndpoint;
    }

    public void setBaseEndpoint(String baseEndpoint) {
        this.baseEndpoint = baseEndpoint;
    }

    public AuthEndpoints getAuth() {
        return auth;
    }

    public void setAuth(AuthEndpoints auth) {
        this.auth = auth;
    }

    public TestEndpoints getTest() {
        return test;
    }

    public void setTest(TestEndpoints test) {
        this.test = test;
    }
}
