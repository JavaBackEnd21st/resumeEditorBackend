package com.team2.resumeeditorproject.user.domain;

import com.team2.resumeeditorproject.resume.domain.ResumeEdit;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.sql.ast.Clause;

import java.util.Date;
import java.util.List;

//@Where(clause="del_date is null")
@Getter
@Setter
@Entity
@Table(name="User")
@SQLDelete(sql = "UPDATE user SET del_date = current_timestamp WHERE u_num = ?") // soft delete
//@Where(clause="del_date is null")
@NoArgsConstructor
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "u_num")
    private Long uNum;
    private String email;
    private String username;
    private String password;
    private String role;
    private int age;
    private String birthDate;
    private char gender;
    private String company;
    private String occupation;
    private String wish;
    private int status;
    private int mode;
    private Date inDate;
    private Date delDate;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)// ResumeEdit와의 양방향 관계를 설정
    private List<ResumeEdit> resumeEdits;

    @Builder
    public User(Long uNum, String email, String username, String password, String role, int age, String birthDate, char gender, String company, String occupation, String wish, int status, int mode, Date inDate, Date delDate) {
        this.uNum = uNum;
        this.email = email;
        this.username=username;
        this.password = password;
        this.role=role;
        this.age = age;
        this.birthDate=birthDate;
        this.gender = gender;
        this.company = company;
        this.occupation = occupation;
        this.wish = wish;
        this.status = status;
        this.mode = mode;
        this.inDate = inDate;
        this.delDate = delDate;
    }
}