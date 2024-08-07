package com.team2.resumeeditorproject.resume.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
/**
 * resumeDTO
 *
 * @author : 안은비
 * @fileName : ResumeDTO
 * @since : 04/25/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDTO {
    private Long resumeNo;
    private String content;
    private Date w_date;
    private Long userNo;
}
