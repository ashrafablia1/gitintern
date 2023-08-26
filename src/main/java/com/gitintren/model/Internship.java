package com.gitintren.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "internships")
public class Internship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "internship_id")
    private Long internshipId;

    @Column(name = "title")
    private String title;


    @Column(name = "location")
    private String location;

    @Column(name = "duration")
    private int duration;

    @Column(name = "description")
    private String description;

    @Column(name = "requirements")
    private String requirements;

    @Column(name = "responsibilities")
    private String responsibilities;

    @Column(name = "qualifications")
    private String qualifications;

    @Column(name = "application_deadline")
    private LocalDate applicationDeadline;

    @Column(name = "contact_email") // can be null
    private String contactEmail;

    @Column(name = "is_paid")
    private boolean isPaid;

    @Column(name = "number_of_positions")
    private int numberOfPositions;

    @Column(name = "is_remote")
    private boolean isRemote;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "company_id")
    private CompanyUser company;



}