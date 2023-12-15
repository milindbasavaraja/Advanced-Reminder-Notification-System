package com.nyu.java.reminderui.frames;

import com.nyu.java.reminderui.client.AuthenticateClient;
import com.nyu.java.reminderui.config.TokenStorage;
import com.nyu.java.reminderui.model.response.JwtValidationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class MainFrame extends JFrame implements ApplicationListener<SwitchFrameEvent> {

    private Logger logger = LoggerFactory.getLogger(MainFrame.class);
    private final CardLayout cardLayout;

    private final JPanel cards;
    private final ApplicationContext applicationContext;
    private JwtValidationResponse response = new JwtValidationResponse();


    public MainFrame(ApplicationContext context, AuthenticateClient authenticateClient) {

        this.applicationContext = context;
        cardLayout = new CardLayout();
        setTitle("Reminders App");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cards = new JPanel(cardLayout);
        try {

            cards.add(context.getBean(LogInFrame.class), "Login");
            cards.add(context.getBean(SignUpFrame.class), "Signup");
            cards.add(context.getBean(WelcomeFrame.class), "Welcome");
            cards.add(context.getBean(ReminderPanels.class), "Reminder");


            add(cards);


            if (TokenStorage.getToken() != null &&
                    !TokenStorage.getToken().isEmpty()) {
                response = authenticateClient.validateJwT(TokenStorage.getToken());
                if(response!= null && response.isTokenValid()){
                    cardLayout.show(cards, "Welcome");
                }
                else{
                    cardLayout.show(cards, "Login");
                }
            } else {
                cardLayout.show(cards, "Login");
            }
        } catch (Exception e) {
            logger.error("Error validating JWT. Backend might be down.", e);

            JOptionPane.showMessageDialog(this,
                    "Unable to connect to the backend service. Please try again later.",
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE);

            cardLayout.show(cards, "Login");
        }

    }


    @Override
    public void onApplicationEvent(SwitchFrameEvent event) {
        logger.info("Switching to frame: {}", event.getFrameName());
        cardLayout.show(cards, event.getFrameName());
        revalidate();
        repaint();

        if ("Welcome".equals(event.getFrameName())) {
            WelcomeFrame welcomeFrame = applicationContext.getBean(WelcomeFrame.class);
            if(response.getUserInfoResponse() == null){
                welcomeFrame.refreshFrame();
            }else{
                welcomeFrame.refreshFrame(response.getUserInfoResponse());
            }

        } else if ("Reminder".equals(event.getFrameName())) {
            ReminderPanels reminderPanels = applicationContext.getBean(ReminderPanels.class);
            reminderPanels.refreshReminders();
        }

    }
}
