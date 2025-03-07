package com.where.auth.service;

import com.where.auth.entity.AppUser;
import com.where.auth.entity.ConfirmationToken;
import com.where.auth.entity.Role;
import com.where.auth.kafka.RegisterTokenEvent;
import com.where.auth.kafka.UserCreateEvent;
import com.where.auth.kafka.UserProducer;
import com.where.auth.kafka.UserRoleUpdateEvent;
import com.where.auth.repository.RoleRepository;
import com.where.auth.repository.UserRepository;
import com.where.auth.utils.ConfirmationTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService, UserDetailsService {
    private final ConfirmationTokenService confirmationTokenService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserProducer userProducer;
    @Value("${auth.confirmation-url}")
    private String confirmationUrl;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = userRepository.findByEmail(email);
        if (user == null) {
            log.error("user not found");
            throw new UsernameNotFoundException("user not found");
        } else {
            log.info("user found {}",email);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    @Override
    public AppUser saveUser(AppUser user) {
        log.info("Saving new user {} to database",user.getName());
        Role userRole = roleRepository.findByName(com.where.enums.Role.USER.name())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(userRole));

        // Send confirmation token
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = ConfirmationTokenUtil.generateConfirmationToken(user);
        String link = confirmationUrl+token;
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        // Send Confirmation Token Link to Email Service
        RegisterTokenEvent registerTokenEvent = new RegisterTokenEvent(
                user.getEmail(),
                user.getName(),
                link
        );
        userProducer.sendRegisterConfirmationEvent(registerTokenEvent);

        // Send saved User to User Service for additional User's detail management
        UserCreateEvent userCreateEvent = new UserCreateEvent(
                user.getId(),
                user.getName(),
                user.getEmail(),
                Set.of(userRole.getName())
        );
        userProducer.sendAppUser(userCreateEvent);

        return userRepository.save(user);
    }


    @Override
    public AppUser getUser(String email) {
        log.info("Fetching user {}",email);
        return userRepository.findByEmail(email);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to database",role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addBusinessOwnerRoleToUser(String email) {
        AppUser user = userRepository.findByEmail(email);
        Role role = roleRepository.findByName(com.where.enums.Role.BUSINESS_OWNER.name()).orElseThrow(() -> new RuntimeException("Role not found"));;
        user.getRoles().add(role);

        Set<String> updatedRoleNames = user.getRoles()
                .stream()
                .map(Role::getName) // Assuming Role has a getName() method
                .collect(Collectors.toSet());
        UserRoleUpdateEvent userRoleUpdateEvent = new UserRoleUpdateEvent(
                user.getEmail(),
                updatedRoleNames
        );
        userProducer.sendUserRoleUpdate(userRoleUpdateEvent);

        userRepository.save(user);
    }

}
