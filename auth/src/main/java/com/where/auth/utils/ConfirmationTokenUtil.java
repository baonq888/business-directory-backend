package com.where.auth.utils;

import com.where.auth.entity.AppUser;
import com.where.auth.entity.ConfirmationToken;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
public class ConfirmationTokenUtil {

    public static ConfirmationToken generateConfirmationToken(AppUser user) {
        return new ConfirmationToken(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
    }
}
