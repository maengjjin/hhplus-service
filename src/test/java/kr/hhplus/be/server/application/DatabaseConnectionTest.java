package kr.hhplus.be.server.application;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DatabaseConnectionTest{



    @Container
    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.33")
        .withDatabaseName("hhplus")
        .withUsername("root")
        .withPassword("newpass123");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        mysqlContainer.start();
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }



    @Test
    void testDatabaseConnection() {
        log.info("로그 getJdbcDriverInstance {} ", mysqlContainer.getJdbcDriverInstance());
        log.info("로그 getJdbcUrl {} ", mysqlContainer.getJdbcUrl());
        log.info("로그 getMappedPort {} ", mysqlContainer.getMappedPort(3306));
        log.info("로그 getHost {} ", mysqlContainer.getHost());
        log.info("로그 getUsername {} ", mysqlContainer.getUsername());
        log.info("로그 getPassword {} ", mysqlContainer.getPassword());
    }


}
