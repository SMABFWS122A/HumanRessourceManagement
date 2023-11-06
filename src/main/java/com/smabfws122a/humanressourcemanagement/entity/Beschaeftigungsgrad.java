package com.smabfws122a.humanressourcemanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.sql.Time;

@Entity
@Getter
public class Beschaeftigungsgrad {
    @Id
    private Integer id;
    private String bezeichnung;
    private double wochenstunden;
    private Integer urlaubsanspruch;
    private Time beginn_arbeitszeitfenster;
    private Time ende_arbeitszeitfenster;

}
