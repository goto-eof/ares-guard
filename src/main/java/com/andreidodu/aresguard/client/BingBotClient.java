package com.andreidodu.aresguard.client;

import com.andreidodu.aresguard.client.common.BotIpRetrieverClient;
import com.andreidodu.aresguard.dto.BingBotDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "bing-bot", url = "${com.andreidodu.ares-guard.bot.bing.query-url}")
public interface BingBotClient extends BotIpRetrieverClient<BingBotDTO> {

    @RequestMapping(method = RequestMethod.GET)
    BingBotDTO retrieveMetaInfo();

}