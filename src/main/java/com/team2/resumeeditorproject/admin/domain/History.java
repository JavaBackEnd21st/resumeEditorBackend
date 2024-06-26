package com.team2.resumeeditorproject.admin.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@Table(name="history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long h_num;

    private int traffic;
    private int edit_count;

    @Column(columnDefinition = "JSON")
    private String user_mode;

    @Column(columnDefinition = "JSON")
    private String user_status;

    @Column(columnDefinition = "JSON")
    private String user_gender;

    @Column(columnDefinition = "JSON")
    private String user_age;

    @Column(columnDefinition = "JSON")
    private String user_occu;

    @Column(columnDefinition = "JSON")
    private String user_comp;

    @Column(columnDefinition = "JSON")
    private String user_wish;

    @Column(columnDefinition = "JSON")
    private String edit_mode;

    @Column(columnDefinition = "JSON")
    private String edit_status;

    @Column(columnDefinition = "JSON")
    private String edit_age;

    @Column(columnDefinition = "JSON")
    private String edit_date;

    @Column(columnDefinition = "JSON")
    private String edit_occu;

    @Column(columnDefinition = "JSON")
    private String edit_comp;

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "timestamp default current_timestamp")
    private Date w_date;

    private LocalDate traffic_date;

}
