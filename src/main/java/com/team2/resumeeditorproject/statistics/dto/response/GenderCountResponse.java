package com.team2.resumeeditorproject.statistics.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GenderCountResponse {
    private int male;
    private int female;
}