package com.andreidodu.aresguard.service.impl;

import com.andreidodu.aresguard.dto.IpPrefixDTO;
import com.andreidodu.aresguard.service.HtaccessService;
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
public class HtaccessServiceImpl implements HtaccessService {
    private final String MARKER_START = "# ares-guard-marker-start";
    private final String MARKER_END = "# ares-guard-marker-end";
    public static final String FILENAME_HTACCESS = ".htaccess";


    @Override
    public void writeOnFile(final String pathString, Map<String, List<IpPrefixDTO>> botsAndIPs, boolean writeFileFlag) throws IOException {
        Path path = Paths.get(pathString + "/" + FILENAME_HTACCESS);


        List<String> allExistingLines = new ArrayList<>();

        try {
            allExistingLines = Files.readAllLines(path);
        } catch (Exception e) {
            log.warn(FILENAME_HTACCESS + " not found");
        }
        List<String> generatedRows = generateLines(botsAndIPs);
        List<String> fullText = insertNewRows(allExistingLines, generatedRows);

        if (writeFileFlag) {
            Files.write(path, fullText);
            log.info("===================================================================");
            log.info("I MODIFIED the " + FILENAME_HTACCESS + " file with the following content");
            log.info("===================================================================");
        } else {
            log.info("================================");
            log.info(FILENAME_HTACCESS + " file was not modified.");
            log.info("================================");
        }
        for (String line : fullText) {
            log.info(line);
        }
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
                    log.debug("removing: {}", line);
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
