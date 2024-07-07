package com.andreidodu.aresguard.service.impl;

import com.andreidodu.aresguard.dto.IpPrefixDTO;
import com.andreidodu.aresguard.service.RobotsTxtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class RobotsTxtServiceImpl implements RobotsTxtService {
    public static final String FILENAME_ROBOTS_TXT = "robots.txt";

    @Override
    public void rewriteRobotsFile(final String pathString, Map<String, List<IpPrefixDTO>> botsAndIPs, boolean writeFileFlag) throws IOException {
        Path path = Paths.get(pathString + "/" + FILENAME_ROBOTS_TXT);

        List<String> lst = new ArrayList<>();
        lst.add("User-agent: *");
        lst.add("Disallow: /");
        lst.add("");

        Set<String> keys = botsAndIPs.keySet();
        keys.forEach(k -> {
            lst.add("User-agent: " + k);
            lst.add("Disallow:");
            lst.add("");
        });

        if (writeFileFlag) {
            Files.write(path, lst);
            log.info("==========================================================");
            log.info("I OVERRIDED the " + FILENAME_ROBOTS_TXT + " file with the following content");
            log.info("==========================================================");
        } else {
            log.info("==========================================================");
            log.info(FILENAME_ROBOTS_TXT + " was not modified.");
            log.info("==========================================================");
        }
        for (String line : lst) {
            log.info(line);
        }

    }
}
