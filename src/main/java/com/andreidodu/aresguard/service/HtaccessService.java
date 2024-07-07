package com.andreidodu.aresguard.service;

import com.andreidodu.aresguard.dto.IpPrefixDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface HtaccessService {
    void writeOnFile(String pathString, Map<String, List<IpPrefixDTO>> botsAndIPs, boolean writeFileFlag) throws IOException;
}
