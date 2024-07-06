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


    private final String MARKER_START = "# ares-guard-marker-start";
    private final String MARKER_END = "# ares-guard-marker-end";


    private final IpRetrieverService ipRetrieverService;

    private static final Logger LOG = LoggerFactory
            .getLogger(AresGuardApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AresGuardApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (args.length != 1) {
            LOG.error("Please specify the root directory of your website. Example: \"/var/www/mywebsite.com\"|");
            return;
        }
        final String pathString = args[0];
        LOG.info("selected path: {}", pathString);
        Map<String, List<IpPrefixDTO>> botsAndIPs = this.ipRetrieverService.retrieveAllIPs();
        try {
            this.writeOnFile(pathString, botsAndIPs);
            this.rewriteRobotsFile(pathString, botsAndIPs);
        } catch (IOException e) {
            LOG.error("Failed to write on .htaccess file: {}", e.toString());
        }

    }

    private void rewriteRobotsFile(final String pathString, Map<String, List<IpPrefixDTO>> botsAndIPs) throws IOException {
        Path path = Paths.get(pathString + "/robots.txt");

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

        Files.write(path, lst);

    }

    private void writeOnFile(final String pathString, Map<String, List<IpPrefixDTO>> botsAndIPs) throws IOException {
        Path path = Paths.get(pathString + "/.htaccess");


        List<String> allExistingLines = new ArrayList<>();

        try {
            allExistingLines = Files.readAllLines(path);
        } catch (Exception e) {
            LOG.warn(".htaccess not found");
        }
        List<String> generatedRows = generateLines(botsAndIPs);
        List<String> fullText = insertNewRows(allExistingLines, generatedRows);

        Files.write(path, fullText);
    }

    private void writeIfStart(List<String> allExistingLines) {
        allExistingLines.add("<IfModule mod_rewrite.c>");
        allExistingLines.add("RewriteEngine On");
    }

    private void writeIfEnd(List<String> currentList) {
        currentList.add("</IfModule>");
    }

    private List<String> insertNewRows(List<String> cleanedLst, List<String> generatedRows) {
        List<String> result = new ArrayList<>();

        boolean inMarked = false;
        boolean found = false;
        for (String line : cleanedLst) {
            if (MARKER_START.equalsIgnoreCase(line.trim())) {
                inMarked = true;
                result.add(line);
                writeIfStart(result);
                result.addAll(generatedRows);
                found = true;
            } else if (MARKER_END.equalsIgnoreCase(line.trim())) {
                writeIfEnd(result);
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
        if (!found) {
            result.add(MARKER_START);
            writeIfStart(result);
            result.addAll(generatedRows);
            writeIfEnd(result);
            result.add(MARKER_END);
        }
        return result;
    }

    private List<String> generateLines(Map<String, List<IpPrefixDTO>> botsAndIPs) {
        Set<String> keys = botsAndIPs.keySet();
        List<String> result = new ArrayList<>();

        for (String key : keys) {
            String header = "RewriteCond %{HTTP_USER_AGENT} ^(.*)" + key + "(.*)$ [NC]";
            result.add(header);
            writeIpList(botsAndIPs, key, result);
            String footer = "RewriteRule ^ - [R=403,L]";
            result.add(footer);
        }

        return result;
    }

    private static void writeIpList(Map<String, List<IpPrefixDTO>> botsAndIPs, String key, List<String> result) {
        for (IpPrefixDTO ipPrefix : botsAndIPs.get(key)) {
            String content = "RewriteCond expr \"! %{REMOTE_ADDR} -ipmatch '" + ipPrefix.getPrefix() + "'\"";
            result.add(content);
        }
    }

}
