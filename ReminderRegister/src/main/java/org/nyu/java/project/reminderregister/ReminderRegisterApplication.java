package org.nyu.java.project.reminderregister;

import org.nyu.java.project.reminderregister.entity.ERole;
import org.nyu.java.project.reminderregister.entity.Role;
import org.nyu.java.project.reminderregister.repository.RoleRepository;
import org.nyu.java.project.reminderregister.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReminderRegisterApplication implements CommandLineRunner {

	@Autowired
	RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(ReminderRegisterApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(roleRepository.findAll().size() == 0){
			roleRepository.save(new Role(ERole.ROLE_USER));
			roleRepository.save(new Role(ERole.ROLE_ADMIN));
		}


	}
}
