package com.andreidodu.aresguard.service.impl.dataextractorstrategy;

import com.andreidodu.aresguard.client.GoogleBotClient;
import com.andreidodu.aresguard.dto.GoogleBotDTO;
import com.andreidodu.aresguard.dto.IpPrefixDTO;
import com.andreidodu.aresguard.dto.IpType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GoogleBotResultExtractorStrategyImpl implements ResultExtractorStrategy {
    private final String NAME = "Googlebot";

    private final GoogleBotClient googleBotClient;

    public String getBotName() {
        return NAME;
    }

    @Override
    public List<IpPrefixDTO> retrieveAndConvert() {
        GoogleBotDTO data = this.retrieve();
        return this.extract(data);
    }

    public GoogleBotDTO retrieve() {
        return this.googleBotClient.retrieveMetaInfo();
    }

    public List<IpPrefixDTO> extract(GoogleBotDTO data) {
        return data.getPrefixes().stream().map(prefix ->
                new IpPrefixDTO(prefix.getIpv4Prefix() != null ? IpType.IP_V4 : IpType.IP_V6, prefix.getIpv4Prefix() != null ? prefix.getIpv4Prefix() : prefix.getIpv6Prefix())
        ).toList();
    }

}
