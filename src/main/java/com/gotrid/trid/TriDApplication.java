package com.gotrid.trid;

import com.gotrid.trid.user.model.Role;
import com.gotrid.trid.user.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
public class TriDApplication {

    public static void main(String[] args) {
        SpringApplication.run(TriDApplication.class, args);
    }

//    @Bean
    public CommandLineRunner runner(
            RoleRepository roleRepository
    ) {
        return args -> {
            if (roleRepository.findByName("ROLE_SELLER").isEmpty()) {
                roleRepository.save(
                        Role.builder()
                                .name("ROLE_SELLER")
                                .build()
                );
            }
            if (roleRepository.findByName("ROLE_USER").isEmpty()) {
                roleRepository.save(
                        Role.builder()
                                .name("ROLE_USER")
                                .build()
                );
            }
        };
    }
}
