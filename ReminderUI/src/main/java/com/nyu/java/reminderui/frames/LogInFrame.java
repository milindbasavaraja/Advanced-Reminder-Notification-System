package com.nyu.java.reminderui.frames;

import com.nyu.java.reminderui.client.AuthenticateClient;
import com.nyu.java.reminderui.config.TokenStorage;
import com.nyu.java.reminderui.exceptions.AuthenticationException;
import com.nyu.java.reminderui.exceptions.EmailNotVerifiedException;
import com.nyu.java.reminderui.model.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component
public class LogInFrame extends JPanel {
    private final JTextField txtUsername;
    private final JPasswordField txtPassword;
    private final JButton btnLogin;

    @Autowired
    public LogInFrame(ApplicationEventPublisher publisher, AuthenticateClient authenticateClient) {
        JButton switchButton = new JButton("Go to Signup");
        switchButton.addActionListener(e ->
                publisher.publishEvent(new SwitchFrameEvent(this, "Signup")));
        add(switchButton);

        txtUsername = new JTextField(15);
        txtPassword = new JPasswordField(15);
        btnLogin = new JButton("Login");


        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Username:"));
        add(txtUsername);
        add(new JLabel("Password:"));
        add(txtPassword);
        add(btnLogin);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = txtUsername.getText();
                char[] password = txtPassword.getPassword();

                LoginRequest user = new LoginRequest(userName, password);
                try {
                    String jwtToken = authenticateClient.login(user);
                    TokenStorage.storeToken(jwtToken);
                    txtUsername.setText("");
                    txtPassword.setText("");
                    publisher.publishEvent(new SwitchFrameEvent(this, "Welcome"));
                } catch (AuthenticationException authenticationException) {
                    JOptionPane.showMessageDialog(LogInFrame.this,
                            authenticationException.getMessage(),
                            "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                } catch (EmailNotVerifiedException ex) {
                    int response = JOptionPane.showConfirmDialog(null, ex.getMessage() + "\nDo you want to verify?", "Email Verification Error", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        String email = JOptionPane.showInputDialog("Enter your email ID");
                        authenticateClient.generateOtpAgain(email);
                        boolean isValid = false;
                        while (!isValid) {
                            try {
                                String otp = JOptionPane.showInputDialog("Enter OTP sent to " + email);
                                authenticateClient.verifyEmail(email, otp);
                                isValid = true;
                                publisher.publishEvent(new SwitchFrameEvent(this, "Welcome"));
                            } catch (AuthenticationException exception) {
                                int response1 = JOptionPane.showConfirmDialog(null, exception.getMessage() + "\nDo you want to retry?", "Email Verification Error", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                                if (response1 == JOptionPane.YES_OPTION) {
                                    if (exception.getMessage().contains("OTP expired.")) {
                                        authenticateClient.generateOtpAgain(email);
                                    }
                                    continue;
                                } else {
                                    break;
                                }
                            }
                        }
                    }

                }


            }
        });

    }


}
