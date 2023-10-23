package com.smabfws122a.humanressourcemanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "zeitbuchung")
@Getter
@Setter
public class Zeitbuchung {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Time uhrzeit;
    private Date datum;
    private String buchungsart;
    private Integer personalnummer;

}
