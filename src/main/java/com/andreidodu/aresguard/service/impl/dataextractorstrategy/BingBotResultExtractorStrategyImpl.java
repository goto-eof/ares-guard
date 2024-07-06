package com.andreidodu.aresguard.service.impl.dataextractorstrategy;

import com.andreidodu.aresguard.client.BingBotClient;
import com.andreidodu.aresguard.dto.BingBotDTO;
import com.andreidodu.aresguard.dto.IpPrefixDTO;
import com.andreidodu.aresguard.dto.IpType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BingBotResultExtractorStrategyImpl implements ResultExtractorStrategy {
    private final String NAME = "Bingbot";

    private final BingBotClient bingBotClient;

    public String getBotName() {
        return NAME;
    }

    @Override
    public List<IpPrefixDTO> retrieveAndConvert() {
        BingBotDTO data = this.retrieve();
        return this.extract(data);
    }

    public BingBotDTO retrieve() {
        return this.bingBotClient.retrieveMetaInfo();
    }

    public List<IpPrefixDTO> extract(BingBotDTO data) {
        return data.getPrefixes().stream().map(prefix ->
                new IpPrefixDTO(prefix.getIpv4Prefix() != null ? IpType.IP_V4 : IpType.IP_V6, prefix.getIpv4Prefix() != null ? prefix.getIpv4Prefix() : prefix.getIpv6Prefix())
        ).toList();
    }

}
