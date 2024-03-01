package com.github.kmpk.twitch;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;

@AutoConfigureDataJpa
@AutoConfigurationPackage(basePackages = "com.github.kmpk")
public abstract class AbstractSpringDataJpaTest {
    @Autowired
    private Flyway flyway;

    @BeforeEach
    void before() {
        flyway.clean();
        flyway.migrate();
    }
}
