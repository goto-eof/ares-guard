package com.andreidodu.aresguard.service.impl;

import com.andreidodu.aresguard.dto.IpPrefixDTO;
import com.andreidodu.aresguard.service.IpRetrieverService;
import com.andreidodu.aresguard.service.impl.dataextractorstrategy.ResultExtractorStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class IpRetrieverServiceImpl implements IpRetrieverService {
    private final List<ResultExtractorStrategy> resultExtractorStrategies;

    @Override
    public Map<String, List<IpPrefixDTO>> retrieveAllIPs() {
        return resultExtractorStrategies
                .stream()
                .collect(Collectors.toMap(ResultExtractorStrategy::getBotName, ResultExtractorStrategy::retrieveAndConvert));
    }

}
