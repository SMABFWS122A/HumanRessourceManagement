package com.smabfws122a.humanressourcemanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Login {
    @Id
    private String email;
    private String passwort;
    private Boolean admin;
    private Integer personalnummer;

}
