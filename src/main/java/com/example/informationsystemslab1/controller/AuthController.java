package com.example.informationsystemslab1.controller;

import com.example.informationsystemslab1.dto.request.auth.LoginRequestDto;
import com.example.informationsystemslab1.dto.request.auth.RegisterRequestDto;
import com.example.informationsystemslab1.dto.response.UserResponseDto;
import com.example.informationsystemslab1.enums.Role;
import com.example.informationsystemslab1.service.ApplicationUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final ApplicationUserService userService;
    private final AuthenticationManager authenticationManager;
    // хранит SecurityContext между запросами в HTTP-сессии (её id уходит клиенту в cookie JSESSIONID)
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public AuthController(ApplicationUserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody RegisterRequestDto dto) {
        UserResponseDto created = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@Valid @RequestBody LoginRequestDto dto,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(dto.username(), dto.password()));

        if (request.getSession(false) != null) {
            request.changeSessionId();
        }

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        securityContextRepository.saveContext(context, request, response);

        return ResponseEntity.ok(userService.getByUsername(authentication.getName()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me(@AuthenticationPrincipal UserDetails principal) {
        UserResponseDto userFromDatabase = userService.getByUsername(principal.getUsername());

        String authority = principal.getAuthorities().iterator().next().getAuthority(); //формат "ROLE_USER"
        Role roleFromSession = Role.valueOf(authority.substring("ROLE_".length()));

        return ResponseEntity.ok(
                new UserResponseDto(userFromDatabase.id(), userFromDatabase.username(), roleFromSession)
        );
    }
}
