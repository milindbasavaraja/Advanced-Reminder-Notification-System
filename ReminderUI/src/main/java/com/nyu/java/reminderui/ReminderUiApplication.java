package com.nyu.java.reminderui;

import com.nyu.java.reminderui.config.PropertiesConfig;
import com.nyu.java.reminderui.frames.MainFrame;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;

@SpringBootApplication
public class ReminderUiApplication {


    public static void main(String[] args) {


        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(ReminderUiApplication.class)
                .headless(false).run(args);

        EventQueue.invokeLater(() -> {
            MainFrame mainFrame = ctx.getBean(MainFrame.class);
            PropertiesConfig propertiesConfig = ctx.getBean(PropertiesConfig.class);
            mainFrame.setVisible(true);
            System.out.println(propertiesConfig.getBaseEndpoint());
            System.out.println(propertiesConfig.getAuth().getLogin());
            System.out.println(propertiesConfig.getTest().getBasicUrl());
        });
    }

}
