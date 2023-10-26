package com.smabfws122a.humanressourcemanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "login")
@Getter
@Setter
public class Login {
    @Id
    private String email;
    private String passwort;
    private Integer admin; //nur 1 oder 0
    private Integer peronalnummmer;

}
