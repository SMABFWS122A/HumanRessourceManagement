package com.smabfws122a.humanressourcemanagement.entity;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.type.NumericBooleanConverter;

@Entity
@Table(name = "login")
@Getter
@Setter
public class Login {
    @Id
    private String email;
    private String passwort;
    @Convert(converter = NumericBooleanConverter.class)
    private Boolean admin;
    private Integer peronalnummmer;

}
