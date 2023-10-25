package com.smabfws122a.humanressourcemanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "zeitbuchung")
@Getter
@Setter
public class Zeitbuchung {

    @Id
    @GeneratedValue(generator = "zeitbuchung-generator")
    @GenericGenerator(
            name = "zeitbuchung-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "zeitbuchung_id_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Integer id;
    private Time uhrzeit;
    private Date datum;
    private String buchungsart;
    private Integer personalnummer;

}
