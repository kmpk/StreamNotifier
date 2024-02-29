package com.github.kmpk.twitch;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan("com.github.kmpk.*.mapper")
public class TestMappersConfig {
}
