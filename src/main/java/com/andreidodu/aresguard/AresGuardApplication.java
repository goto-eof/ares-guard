package com.andreidodu.aresguard;

import com.andreidodu.aresguard.dto.IpPrefixDTO;
import com.andreidodu.aresguard.enums.ActionEnum;
import com.andreidodu.aresguard.service.HtaccessService;
import com.andreidodu.aresguard.service.IpRetrieverService;
import com.andreidodu.aresguard.service.RobotsTxtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class AresGuardApplication implements CommandLineRunner {

    private final IpRetrieverService ipRetrieverService;
    private final HtaccessService htaccessService;
    private final RobotsTxtService robotsTxtService;

    public static void main(String[] args) {
        SpringApplication.run(AresGuardApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (args.length == 0 || args.length > 2) {
            log.error("Please specify the path to te root directory of your website. Example: \"/var/www/mywebsite.com\"|");
            return;
        }
        final String pathString = args[0];
        log.info("selected path: {}", pathString);
        ActionEnum actionEnum = ActionEnum.UNDEFINED;
        if (args.length > 1) {
            final String action = args[1];
            actionEnum = ActionEnum.fromString(action);
            log.info("action: {} ({})", action, actionEnum);
        } else {
            log.info("action: {}", actionEnum);
        }
        Map<String, List<IpPrefixDTO>> botsAndIPs = this.ipRetrieverService.retrieveAllIPs();
        try {
            boolean writeFileFlag = actionEnum.equals(ActionEnum.WRITE_FILE);
            htaccessService.writeOnFile(pathString, botsAndIPs, writeFileFlag);
            robotsTxtService.rewriteRobotsFile(pathString, botsAndIPs, writeFileFlag);
        } catch (IOException e) {
            log.error("Failed to write one or more files: {}", e.toString());
        }

    }


}
