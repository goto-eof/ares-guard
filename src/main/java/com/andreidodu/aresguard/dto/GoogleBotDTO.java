package com.andreidodu.aresguard.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GoogleBotDTO {
    private String creationTime;
    private List<GoogleBotPrefixDTO> prefixes;
}
