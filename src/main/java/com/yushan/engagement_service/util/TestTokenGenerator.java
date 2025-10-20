package com.yushan.engagement_service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TestTokenGenerator implements CommandLineRunner {

    @Autowired
    private JwtTestUtil jwtTestUtil;

    @Override
    public void run(String... args) {
        String token = jwtTestUtil.generateAccessToken(
                UUID.randomUUID(),           // random userID
                "testuser@yushan.com",       // email
                "USER"                       // role, can be "USER", "AUTHOR", "ADMIN"
        );
        System.out.println("\n=== TEST JWT TOKEN ===\nBearer " + token + "\n=====================\n");
    }
}
