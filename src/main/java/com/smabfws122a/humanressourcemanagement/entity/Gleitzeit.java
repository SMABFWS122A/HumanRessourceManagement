package com.smabfws122a.humanressourcemanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Gleitzeit {

    @Id
    @GeneratedValue(generator = "gleitzeit-generator")
    @GenericGenerator(
            name = "gleitzeit-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "gleitzeit_id_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Integer id;
    private Integer gleitzeitsaldo;
    private Date datum;
    @CreationTimestamp
    private LocalDateTime zeitstempel;
    private Integer personalnummer;

}
