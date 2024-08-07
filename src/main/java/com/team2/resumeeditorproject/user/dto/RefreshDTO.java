package com.team2.resumeeditorproject.user.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RefreshDTO {
    private Long refreshNo;
    private String username;
    private String refresh;
    private Date expirationDate;
}
