package com.andreidodu.aresguard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class IpPrefixDTO {
    private IpType ipType;
    private String prefix;
}
