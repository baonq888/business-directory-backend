package com.where.e2e_tests.utils;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseCleaner {

    private static final List<DatabaseConfig> DATABASES = Arrays.asList(
            new DatabaseConfig("jdbc:postgresql://localhost:5432/auth_db", "postgres", "password", "auth_db"),
            new DatabaseConfig("jdbc:postgresql://localhost:5432/user_db", "postgres", "password", null),
            new DatabaseConfig("jdbc:postgresql://localhost:5432/business_db", "postgres", "password", null),
            new DatabaseConfig("jdbc:postgresql://localhost:5432/review_db", "postgres", "password", null)
    );

    @BeforeAll
    public static void resetDatabases() throws Exception {
        for (DatabaseConfig dbConfig : DATABASES) {
            System.out.println("Cleaning database: " + dbConfig.url);

            try (Connection conn = DriverManager.getConnection(dbConfig.url, dbConfig.username, dbConfig.password);
                 Statement stmt = conn.createStatement()) {

                String sql = getSql(dbConfig);

                stmt.execute(sql);
                System.out.println("Cleaned: " + dbConfig.url);

            } catch (Exception e) {
                System.err.println("Failed to clean: " + dbConfig.url);
                e.printStackTrace();
            }
        }
    }

    private static @NotNull String getSql(DatabaseConfig dbConfig) {
        return """
        DO $$
        DECLARE
            r RECORD;
        BEGIN
            -- Drop sequences except confirmation_token_sequence
            FOR r IN (
                SELECT sequence_name
                FROM information_schema.sequences
                WHERE sequence_schema = 'public'
                AND sequence_name != 'confirmation_token_sequence'
            ) LOOP
                EXECUTE 'DROP SEQUENCE IF EXISTS ' || quote_ident(r.sequence_name) || ' CASCADE';
            END LOOP;

            -- Truncate all tables except 'role' and 'app_user'
            FOR r IN (
                SELECT tablename
                FROM pg_tables
                WHERE schemaname = 'public'
                AND tablename NOT IN ('role', 'app_user')
            ) LOOP
                EXECUTE 'TRUNCATE TABLE ' || quote_ident(r.tablename) || ' CASCADE';
            END LOOP;

            -- DELETE non-admin users from app_user
            EXECUTE 'DELETE FROM app_user_roles WHERE app_user_user_id IN (SELECT user_id FROM app_user WHERE email != ''admin@example.com'')';
            EXECUTE 'DELETE FROM app_user WHERE email != ''admin@example.com''';

            -- Reset sequences (excluding confirmation_token_sequence)
            FOR r IN (
                SELECT sequence_name
                FROM information_schema.sequences
                WHERE sequence_schema = 'public'
                AND sequence_name != 'confirmation_token_sequence'
            ) LOOP
                EXECUTE 'SELECT setval(''' || r.sequence_name || ''', COALESCE((SELECT MAX(id) FROM ' || quote_ident(r.sequence_name) || '), 1), false)';
            END LOOP;
        END $$;
        """;
    }

    private static class DatabaseConfig {
        String url;
        String username;
        String password;
        String name;

        public DatabaseConfig(String url, String username, String password, String name) {
            this.url = url;
            this.username = username;
            this.password = password;
            this.name = name;
        }
    }

    @Test
    public void dummyTest() {
    }
}