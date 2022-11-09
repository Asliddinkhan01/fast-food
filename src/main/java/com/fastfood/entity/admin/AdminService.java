package com.fastfood.entity.admin;


import com.fastfood.common.ApiResponse;
import com.fastfood.entity.food.Category;
import com.fastfood.entity.role.Role;
import com.fastfood.entity.role.RoleRepository;
import com.fastfood.entity.user.User;
import com.fastfood.entity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    public HttpEntity<?> changeRole(String phoneNumber, String role) {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("No user", false), HttpStatus.NOT_FOUND);
        }

        Optional<Role> optionalRole = roleRepository.findByName(role);

        User user = optionalUser.get();

        Set<Role> roles = user.getRoles();

        long num = roles.stream()
                .filter(role1 -> role1.getName().compareTo(role) == 0)
                .count();

        if (num == 0) {
            roles.add(optionalRole.get());
        } else {
            roles.remove(optionalRole.get());
        }

        user.setRoles(roles);

        userRepository.save(user);

        return new ResponseEntity<>(new ApiResponse("Done Success", true), HttpStatus.OK);
    }
}
