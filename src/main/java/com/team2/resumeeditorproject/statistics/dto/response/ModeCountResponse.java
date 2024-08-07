package com.team2.resumeeditorproject.statistics.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ModeCountResponse {
    @JsonProperty("light")
    private int lightMode;

    @JsonProperty("pro")
    private int proMode;
}
