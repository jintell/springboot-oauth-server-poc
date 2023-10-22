package com.jae.platform.configuration;

import com.jae.platform.service.GreetingAppService;
import com.jae.platform.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Josiah Adetayo
 * @Email: josleke@gmail.com, josiah.adetayo@meld-tech.com
 * @Date: 9/21/23
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class CLIApplication implements CommandLineRunner {

    private final TokenService tokenService;
    private final GreetingAppService appService;
    @Override
    public void run(String... args) throws Exception {
        tokenService.getAccessToken()
                .flatMap(appService::getGreetings)
                .subscribe(greetings -> log.info("Greetings: {}", greetings));
    }
}
