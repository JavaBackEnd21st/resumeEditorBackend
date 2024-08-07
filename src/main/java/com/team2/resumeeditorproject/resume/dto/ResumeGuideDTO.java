package com.team2.resumeeditorproject.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeGuideDTO {
    private Long resumeGuideNo;
    private Long userNo;
    private Long companyNo;
    private String companyName;
    private Long occupationNo;
    private String occupationName;
    private String content;
    private String questions;
    private int mode;
}
