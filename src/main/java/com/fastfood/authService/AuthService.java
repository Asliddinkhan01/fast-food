package com.fastfood.authService;

import com.fastfood.common.ApiResponse;
import com.fastfood.entity.role.Role;
import com.fastfood.entity.role.RoleRepository;
import com.fastfood.entity.sms.SmsService;
import com.fastfood.entity.user.User;
import com.fastfood.entity.user.UserRepository;
import com.fastfood.security.JwtProvider;
import com.fastfood.util.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final SmsService smsService;


    public HttpEntity<?> registerUser(RegisterDto registerDto) {

        if (userRepository.existsByPhoneNumber(registerDto.getPhoneNumber()))
            return ResponseEntity.status(409).body(new ApiResponse("This user registered before", false));

        Set<Role> roleUser = new HashSet<>();

        Optional<Role> optionalRole = roleRepository.findByName(AppConstants.USER);
        if (optionalRole.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Something went wrong", false), HttpStatus.CONFLICT);
        }

        Role userRole = optionalRole.get();
        roleUser.add(userRole);

        User user = new User(
                registerDto.getFullName(),
                registerDto.getPhoneNumber(),
                passwordEncoder.encode(registerDto.getSmsCode()),
                roleUser
        );

        User savedUser;
        try {
            savedUser = userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ApiResponse("this user already exists", false), HttpStatus.CONFLICT);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                savedUser.getPhoneNumber(), registerDto.getSmsCode());
        authenticationManager.authenticate(authenticationToken);

        String token = jwtProvider.generateToken(user.getPhoneNumber(), user.getRoles());

        return ResponseEntity.status(200).body(new ApiResponse("Registered Successfully", true, token));
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new UsernameNotFoundException(phoneNumber));
    }

    public HttpEntity<?> loginUser(@Valid LoginDto loginDto) {

        ApiResponse apiResponse = smsService.validateSmsCode(loginDto.getPhoneNumber(), loginDto.getCodeSent());
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(404).body(apiResponse);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getPhoneNumber(), loginDto.getCodeSent());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        User user = (User) authenticate.getPrincipal();
        String token = jwtProvider.generateToken(user.getPhoneNumber(), user.getRoles());

        return ResponseEntity.status(200).body(new ApiResponse("Successful signed in.", true, token));
    }

}
