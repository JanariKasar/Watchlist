package com.fakebank.Watchlist.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "sanctions")
public class SanctionedPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column
    private String middleName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDateTime startDate = LocalDateTime.now();

    @Column
    private LocalDateTime endDate;

    public SanctionedPerson(String firstName, String middleName, String lastName, LocalDateTime startDate) {
        this(firstName, middleName, lastName, startDate, null);
    }

    public SanctionedPerson(String firstName, String middleName, String lastName, LocalDateTime startDate, LocalDateTime endDate) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
