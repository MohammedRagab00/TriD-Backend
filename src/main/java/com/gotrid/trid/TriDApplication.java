package com.gotrid.trid;

import com.gotrid.trid.core.user.model.Role;
import com.gotrid.trid.core.user.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
@EnableScheduling
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
        };
    }
}
