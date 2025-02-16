package com.where.user.kafka;

import com.where.user.entity.Role;
import com.where.user.entity.UserDetail;
import com.where.user.repository.RoleRepository;
import com.where.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserConsumer {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @KafkaListener(topics = "user-create-topic", groupId = "user-service-group")
    public void consumeUserCreatedEvent(UserCreateEvent event) {
        System.out.println("Received new user: " + event.getUsername());

        // Fetch only existing roles from the database
        Set<Role> roles = event.getRoles().stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseGet(() -> roleRepository.save(new Role(roleName))))
                .collect(Collectors.toSet());

        // Create and save UserDetail
        UserDetail userDetail = new UserDetail();
        userDetail.setId(event.getId());
        userDetail.setName(event.getName());
        userDetail.setUsername(event.getUsername());
        userDetail.setRoles(roles);

        userRepository.save(userDetail);
    }

    @KafkaListener(topics = "user-role-update-topic", groupId = "user-service-group")
    public void consumeUserRoleUpdateEvent(UserCreateEvent event) {


        // TODO: Update role for user
    }

}
