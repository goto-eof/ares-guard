package com.andreidodu.aresguard.service.impl.dataextractorstrategy;

import com.andreidodu.aresguard.dto.IpPrefixDTO;

import java.util.List;

public interface ResultExtractorStrategy {
    String getBotName();

    List<IpPrefixDTO> retrieveAndConvert();
}
