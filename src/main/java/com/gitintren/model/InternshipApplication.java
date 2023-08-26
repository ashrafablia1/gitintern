package com.gitintren.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "internship_application")
public class InternshipApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intern_id")
    private InternUser internUser;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "internship_id")
    private Internship internship;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name="country")
    private String country;

    @Column(name = "city")
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;


    public InternshipApplication(Internship internship, InternUser intern) {
        this.internship = internship;
        this.internUser = intern;
        this.firstName = intern.getFirstName();
        this.lastName = intern.getLastName();
        this.email = intern.getUser().getEmail();
        this.phoneNumber = intern.getPhoneNumber();
        this.country = intern.getCountry();
        this.city = intern.getCity();
        this.status = Status.PENDING;
    }


}

