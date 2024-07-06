package com.andreidodu.aresguard.service;

import com.andreidodu.aresguard.dto.IpPrefixDTO;

import java.util.List;
import java.util.Map;

public interface IpRetrieverService {
    Map<String, List<IpPrefixDTO>> retrieveAllIPs();
}
