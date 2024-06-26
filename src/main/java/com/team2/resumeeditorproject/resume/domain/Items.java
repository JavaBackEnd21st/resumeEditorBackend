
package com.team2.resumeeditorproject.resume.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Getter
@Setter
@Entity
@Table(name="items")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class Items {
    @Id
    @Column(nullable = false, name = "company")
    private String company;
    @Column(nullable = false, name = "items")
    private String items;

}
