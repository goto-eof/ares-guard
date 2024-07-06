package com.andreidodu.aresguard;

import com.andreidodu.aresguard.dto.IpPrefixDTO;
import com.andreidodu.aresguard.service.IpRetrieverService;
import lombok.RequiredArgsConstructor;
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

@SpringBootApplication
@RequiredArgsConstructor
public class AresGuardApplication implements CommandLineRunner {


    private final String MARKER_START = "# marker-start";
    private final String MARKER_END = "# marker-end";


    private final IpRetrieverService ipRetrieverService;

    private static final Logger LOG = LoggerFactory
            .getLogger(AresGuardApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AresGuardApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Map<String, List<IpPrefixDTO>> botsAndIPs = this.ipRetrieverService.retrieveAllIPs();
        try {
            this.writeOnFile(botsAndIPs);
        } catch (IOException e) {
            LOG.error("Failed to write on .htaccess file: {}", e.toString());
        }

    }

    private void writeOnFile(Map<String, List<IpPrefixDTO>> botsAndIPs) throws IOException {
        Path path = Paths.get("/home/andrei/Desktop/.htaccess");

        List<String> lst = Files.readAllLines(path);

        List<String> generatedRows = generateLines(botsAndIPs);
        List<String> fullText = insertNewRows(lst, generatedRows);

        Files.write(path, fullText);
    }

    private List<String> insertNewRows(List<String> cleanedLst, List<String> generatedRows) {
        List<String> result = new ArrayList<>();

        boolean inMarked = false;
        for (String line : cleanedLst) {
            if (MARKER_START.equalsIgnoreCase(line.trim())) {
                inMarked = true;
                result.add(line);
                result.addAll(generatedRows);
            } else if (MARKER_END.equalsIgnoreCase(line.trim())) {
                result.add(line);
                inMarked = false;
            } else {
                if (inMarked) {
                    LOG.info("removing: {}", line);
                } else {
                    result.add(line);
                }
            }
        }

        return result;
    }

    private List<String> generateLines(Map<String, List<IpPrefixDTO>> botsAndIPs) {
        Set<String> keys = botsAndIPs.keySet();
        List<String> result = new ArrayList<>();

        for (String key : keys) {
            String header = "RewriteCond %{HTTP_USER_AGENT} ^(.*)" + key + "(.*)$ [NC]";
            result.add(header);
            for (IpPrefixDTO ipPrefix : botsAndIPs.get(key)) {
                String content = "RewriteCond expr \"! %{REMOTE_ADDR} -ipmatch '" + ipPrefix.getPrefix() + "'\"";
                result.add(content);
            }
            String footer = "RewriteRule ^ - [R=403,L]";
            result.add(footer);

            LOG.info("{} -> {}", key, botsAndIPs.get(key));
        }

        return result;
    }

}
