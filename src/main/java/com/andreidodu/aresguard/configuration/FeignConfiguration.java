package com.andreidodu.aresguard.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.andreidodu.aresguard.client")
public class FeignConfiguration {
}
