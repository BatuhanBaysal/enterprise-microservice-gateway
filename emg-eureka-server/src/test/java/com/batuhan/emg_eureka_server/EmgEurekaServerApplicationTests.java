package com.batuhan.emg_eureka_server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EurekaServerAutoConfiguration;

@SpringBootTest
@EnableAutoConfiguration(exclude = {
        EurekaServerAutoConfiguration.class,
        SimpleDiscoveryClientAutoConfiguration.class
})
class EmgEurekaServerApplicationTests {

	@Test
	void contextLoads() {

	}
}