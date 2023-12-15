package com.nyu.java.reminderui.frames;

import com.nyu.java.reminderui.client.AuthenticateClient;
import com.nyu.java.reminderui.exceptions.AuthenticationException;
import com.nyu.java.reminderui.model.request.SignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component
public class SignUpFrame extends JPanel {
    private JTextField txtUsername;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnSignup;


    private ApplicationEventPublisher publisher;

    @Autowired
    public SignUpFrame(ApplicationEventPublisher publisher, AuthenticateClient authenticateClient) {

        JButton switchButton = new JButton("Go to Login");
        switchButton.addActionListener(e ->
                publisher.publishEvent(new SwitchFrameEvent(this, "Login")));
        add(switchButton);

        txtUsername = new JTextField(15);
        txtEmail = new JTextField(15);
        txtPassword = new JPasswordField(15);
        btnSignup = new JButton("Signup");

        this.publisher = publisher;


        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Username:"));
        add(txtUsername);
        add(new JLabel("Email:"));
        add(txtEmail);
        add(new JLabel("Password:"));
        add(txtPassword);
        add(btnSignup);

        btnSignup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = txtUsername.getText();
                char[] password = txtPassword.getPassword();
                String email = txtEmail.getText();

                SignUpRequest newUser = new SignUpRequest(userName, password, email);
                try {
                    boolean isSuccessful = authenticateClient.signUp(newUser);

                    if (isSuccessful) {
                        txtUsername.setText("");
                        txtPassword.setText("");
                        txtEmail.setText("");
                        showOtpVerificationPopup(email, authenticateClient);

                    } else {
                        throw new AuthenticationException("Signup failed: ");
                    }
                } catch (AuthenticationException authenticationException) {
                    JOptionPane.showMessageDialog(SignUpFrame.this,
                            authenticationException.getMessage(),
                            "SignUp Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });


    }

    private void showOtpVerificationPopup(String email, AuthenticateClient authenticateClient) throws AuthenticationException {
        boolean isValid = false;

        while (!isValid) {
            try {
                String otp = JOptionPane.showInputDialog(this, "Enter OTP sent to " + email);
                authenticateClient.verifyEmail(email, otp);
                isValid = true;
            } catch (AuthenticationException e) {
                int response = JOptionPane.showConfirmDialog(null, e.getMessage() + "\nDo you want to retry?", "Email Verification Error", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    if (e.getMessage().contains("OTP expired.")) {
                        authenticateClient.generateOtpAgain(email);
                    }
                    continue;
                } else {
                    break;
                }
            }
        }

        publisher.publishEvent(new SwitchFrameEvent(this, "Login"));


    }
}
