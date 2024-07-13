package com.team2.resumeeditorproject.resume.domain;

import java.util.Date;

import com.team2.resumeeditorproject.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
public class Guide {
    @Id
    private Long guideNo;
    private String awards;
    private String experiences;
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "user_no")
    private User user;
}
