package org.nyu.java.project.reminderregister;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReminderRegisterApplication {



    public static void main(String[] args) {
        SpringApplication.run(ReminderRegisterApplication.class, args);
    }


}
