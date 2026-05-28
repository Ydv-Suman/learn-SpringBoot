package com.suman.expensetracker.auth.controller;

import com.suman.expensetracker.dto.LoginRequestDto;
import com.suman.expensetracker.dto.LoginResponseDto;
import com.suman.expensetracker.dto.RegisterRequestDto;
import com.suman.expensetracker.dto.UserDto;
import com.suman.expensetracker.entity.ExpenseTrackerUser;
import com.suman.expensetracker.repository.ExpenseTrackerUserRepository;
import com.suman.expensetracker.security.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Locale;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final PasswordEncoder passwordEncoder;
    private final ExpenseTrackerUserRepository expenseTrackerUserRepository;
    private final CompromisedPasswordChecker compromisedPasswordChecker;
    private final JWTUtil jwtUtil;


    @PostMapping("/login")
    private ResponseEntity<LoginResponseDto> loginUser(@RequestBody LoginRequestDto loginRequestDto) {

        try{
            String normalizedEmail = loginRequestDto.email().trim().toLowerCase(Locale.ROOT);
            var resultAuthentication = authenticationConfiguration.getAuthenticationManager()
                    .authenticate(new UsernamePasswordAuthenticationToken(normalizedEmail, loginRequestDto.password()));
            String jwtToken = jwtUtil.generateJwtToekn(resultAuthentication);
            UserDto userDto = expenseTrackerUserRepository.findByEmail(normalizedEmail)
                    .map(user -> {
                        UserDto dto = new UserDto();
                        dto.setName(user.getName());
                        dto.setEmail(user.getEmail());
                        return dto;
                    })
                    .orElseGet(UserDto::new);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new LoginResponseDto("Login Successful",
                            userDto,
                            jwtToken));

        } catch (BadCredentialsException ex) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                    "Invalid username or password");
        } catch (AuthenticationException ex) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                    "Authentication failed");
        } catch (Exception ex) {
            return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred");
        }
    }

    private ResponseEntity<LoginResponseDto> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new LoginResponseDto(message, null, null));
    }


    @PostMapping("/register")
    private ResponseEntity<?> registerUser(@RequestBody RegisterRequestDto registerRequestDto) {

        CompromisedPasswordDecision decision = compromisedPasswordChecker
                .check(registerRequestDto.password());
        if (decision.isCompromised()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("password", "Choose a strong password"));
        }

        String normalizedEmail = registerRequestDto.email().trim().toLowerCase(Locale.ROOT);
        Optional<ExpenseTrackerUser> existingUser = expenseTrackerUserRepository.readUserByEmail(normalizedEmail);
        if (existingUser.isPresent()) {
            Map<String, String> errors = new HashMap<>();
            errors.put("email", "Email already exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        ExpenseTrackerUser user = new ExpenseTrackerUser();
        BeanUtils.copyProperties(registerRequestDto, user);
        user.setEmail(normalizedEmail);
        user.setPasswordHash(passwordEncoder.encode(registerRequestDto.password()));
        user.setCreatedAt(user.getUpdatedAt());
        user.setUpdatedAt(user.getCreatedAt());
        expenseTrackerUserRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "User Registered Successfully"));
    }

}
