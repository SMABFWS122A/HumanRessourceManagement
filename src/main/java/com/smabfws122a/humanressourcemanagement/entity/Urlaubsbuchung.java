package com.smabfws122a.humanressourcemanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Date;

@Entity
@Getter
@Setter
public class Urlaubsbuchung {

    @Id
    @GeneratedValue(generator = "urlaubsbuchung-generator")
    @GenericGenerator(
            name = "urlaubsbuchung-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "urlaubsbuchung_id_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "7"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Integer id;
    @Column(name = "von_datum")
    private Date vonDatum;
    @Column(name = "bis_datum")
    private Date bisDatum;
    private String buchungsart;
    private Integer personalnummer;

}