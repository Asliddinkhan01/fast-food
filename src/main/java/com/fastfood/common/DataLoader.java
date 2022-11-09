package com.fastfood.common;

import com.fastfood.entity.role.Role;
import com.fastfood.entity.role.RoleRepository;
import com.fastfood.entity.user.User;
import com.fastfood.entity.user.UserRepository;
import com.fastfood.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    @Value("${spring.sql.init.mode}")
    private String initialMode;

    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("always")) {


            Role admin = roleRepository.save(new Role(
                    AppConstants.ADMIN,
                    "Admin"
            ));
            Role user = roleRepository.save(new Role(
                    AppConstants.USER,
                    "User"
            ));

            Role chef = roleRepository.save(new Role(
                    AppConstants.CHEF,
                    "Chef"
            ));

            Role deliverer = roleRepository.save(new Role(
                    AppConstants.DELIVERER,
                    "Deliverer"
            ));

            userRepository.save(new User(
                    "Asliddin",
                    "+998903411511",
                    passwordEncoder.encode("admin"),
                    new HashSet<>(List.of(admin, user))
            ));

            userRepository.save(new User(
                    "Asliddin",
                    "+998903705710",
                    passwordEncoder.encode("user"),
                    new HashSet<>(List.of(user))
            ));

        }
    }
}
