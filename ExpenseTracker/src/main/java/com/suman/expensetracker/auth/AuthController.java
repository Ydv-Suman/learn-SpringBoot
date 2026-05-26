package com.suman.expensetracker.auth;

import com.suman.expensetracker.dto.RegisterRequestDto;
import com.suman.expensetracker.entity.ExpenseTrackerUser;
import com.suman.expensetracker.repository.ExpenseTrackerUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final PasswordEncoder passwordEncoder;
    private final ExpenseTrackerUserRepository ExpenseTrackerUserRepository;

    @PostMapping("/login")
    private ResponseEntity<String> loginUser() {
        return ResponseEntity.ok("Logged in");
    }

    @PostMapping("/register")
    private ResponseEntity<String> registerUser(@RequestBody RegisterRequestDto registerRequestDto) {
        ExpenseTrackerUser user = new ExpenseTrackerUser();
        BeanUtils.copyProperties(registerRequestDto, user);
        user.setPasswordHash(passwordEncoder.encode(registerRequestDto.password()));
        ExpenseTrackerUserRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User Registered Successfully");
    }

}
