package com.andreidodu.aresguard.client;

import com.andreidodu.aresguard.client.common.BotIpRetrieverClient;
import com.andreidodu.aresguard.dto.GoogleBotDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "google-bot", url = "${com.andreidodu.ares-guard.bot.google.query-url}")
public interface GoogleBotClient extends BotIpRetrieverClient<GoogleBotDTO> {

    @RequestMapping(method = RequestMethod.GET)
    GoogleBotDTO retrieveMetaInfo();

}