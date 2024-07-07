package com.andreidodu.aresguard.service;

import com.andreidodu.aresguard.dto.IpPrefixDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface RobotsTxtService {
    void rewriteRobotsFile(String pathString, Map<String, List<IpPrefixDTO>> botsAndIPs, boolean writeFileFlag) throws IOException;
}
