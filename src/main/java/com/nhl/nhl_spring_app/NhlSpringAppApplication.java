package com.nhl.nhl_spring_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.nhl.nhl_spring_app.model.AppUser;
import com.nhl.nhl_spring_app.repository.AppUserRepository;

@SpringBootApplication
public class NhlSpringAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(NhlSpringAppApplication.class, args);
	}

	@Bean
	CommandLineRunner initAdmin(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.findByUsername("admin").isEmpty()) {
				AppUser admin = new AppUser();
				admin.setUsername("admin");
				admin.setPassword(passwordEncoder.encode("1234"));
				admin.setRole("ROLE_ADMIN");
				admin.setEmail("admin@nhlapp.com");
				userRepository.save(admin);
				System.out.println("Usuário Admin criado com sucesso! (admin/1234)");
			}
		};
	}
}
