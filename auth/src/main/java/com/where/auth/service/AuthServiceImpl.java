package com.where.auth.service;

import com.where.auth.entity.AppUser;
import com.where.auth.entity.Role;
import com.where.auth.enums.AppUserRole;
import com.where.auth.kafka.UserCreateEvent;
import com.where.auth.kafka.UserProducer;
import com.where.auth.repository.RoleRepository;
import com.where.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Service
@Transactional
@Slf4j
public class AuthServiceImpl implements AuthService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserProducer userProducer;
    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserProducer userProducer) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;

        this.userProducer = userProducer;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("user not found");
            throw new UsernameNotFoundException("user not found");
        } else {
            log.info("user found {}",username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    @Override
    public AppUser saveUser(AppUser user) {
        log.info("Saving new user {} to database",user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName(AppUserRole.USER.name())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        UserCreateEvent userCreateEvent = new UserCreateEvent(
                user.getId(),
                user.getName(),
                user.getUsername(),
                Set.of(userRole.getName())
        );
        userProducer.sendAppUser(userCreateEvent);
        return userRepository.save(user);
    }


    @Override
    public AppUser getUser(String username) {
        log.info("Fetching user {}",username);
        return userRepository.findByUsername(username);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to database",role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addBusinessOwnerRoleToUser(String username) {
        AppUser user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(AppUserRole.BUSINESS_OWNER.name()).orElseThrow(() -> new RuntimeException("Role not found"));;
        user.getRoles().add(role);
        userRepository.save(user);
    }

}
