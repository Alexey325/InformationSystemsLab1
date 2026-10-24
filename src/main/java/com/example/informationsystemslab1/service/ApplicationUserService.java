package com.example.informationsystemslab1.service;

import com.example.informationsystemslab1.dto.request.auth.RegisterRequestDto;
import com.example.informationsystemslab1.dto.response.UserResponseDto;
import com.example.informationsystemslab1.entity.ApplicationUser;
import com.example.informationsystemslab1.enums.Role;
import com.example.informationsystemslab1.exception.custom.EntityNotFoundException;
import com.example.informationsystemslab1.exception.custom.UsernameAlreadyExistsException;
import com.example.informationsystemslab1.mapper.ApplicationUserMapper;
import com.example.informationsystemslab1.repository.ApplicationUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserMapper userMapper;

    public ApplicationUserService(ApplicationUserRepository applicationUserRepository,
                                  PasswordEncoder passwordEncoder,
                                  ApplicationUserMapper userMapper) {
        this.userRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ApplicationUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username with " + username + " was not found"));

        return User.withUsername(user.getUsername())
                .password(user.getHashPassword())
                .roles(user.getRole().name())
                .build();
    }

    public UserResponseDto register(RegisterRequestDto dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new UsernameAlreadyExistsException(dto.username());
        }

        ApplicationUser user = new ApplicationUser();
        user.setUsername(dto.username());
        user.setHashPassword(passwordEncoder.encode(dto.password()));
        user.setRole(Role.USER);

        return userMapper.toDto(userRepository.save(user));
    }

    public UserResponseDto getByUsername(String username) {
        ApplicationUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ApplicationUser.class, username));

        return userMapper.toDto(user);
    }
}
