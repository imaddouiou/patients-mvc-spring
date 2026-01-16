package com.douiou0.patientsmvc;

import com.douiou0.patientsmvc.entities.Patient;
import com.douiou0.patientsmvc.repositories.PatientRepository;
import com.douiou0.patientsmvc.sec.service.SecurityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@SpringBootApplication
public class PatientsMvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatientsMvcApplication.class, args);
	}
    @Bean
    PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder();
    }
    //    @Bean
    CommandLineRunner commandLineRunner(PatientRepository patientRepository) {
        return args -> {
            patientRepository.save(new Patient(null,"Hassan",new Date(),false,122));
            patientRepository.save(new Patient(null,"Mohamed",new Date(),true,412));
            patientRepository.save(new Patient(null,"Yassmine",new Date(),true,142));
            patientRepository.save(new Patient(null,"Hanae",new Date(),false,132));
            patientRepository.findAll().forEach(p->{
                System.out.println(p.getNom());
            });
        };
    }
    @Bean
    CommandLineRunner saveUsers(SecurityService securityService) {
        return args -> {

            // ===== USERS =====
            if (!securityService.userExists("mohamed")) {
                securityService.saveNewUser("mohamed", "1234", "1234");
            }

            if (!securityService.userExists("yasmine")) {
                securityService.saveNewUser("yasmine", "1234", "1234");
            }

            if (!securityService.userExists("hassan")) {
                securityService.saveNewUser("hassan", "1234", "1234");
            }

            // ===== ROLES =====
            try {
                securityService.saveNewRole("USER", "");
            } catch (Exception ignored) {}

            try {
                securityService.saveNewRole("ADMIN", "");
            } catch (Exception ignored) {}

            // ===== ROLE → USER =====
            securityService.addRoleToUser("mohamed", "USER");
            securityService.addRoleToUser("mohamed", "ADMIN");
            securityService.addRoleToUser("yasmine", "USER");
            securityService.addRoleToUser("hassan", "USER");
        };
    }

}
