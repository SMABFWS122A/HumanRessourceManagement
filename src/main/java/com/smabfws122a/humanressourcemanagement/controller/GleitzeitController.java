package com.smabfws122a.humanressourcemanagement.controller;

import com.smabfws122a.humanressourcemanagement.entity.Gleitzeit;
import com.smabfws122a.humanressourcemanagement.service.GleitzeitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GleitzeitController {

    @Autowired
    GleitzeitService service;

    @GetMapping(value = "/gleitzeit/{personalnummer}")
    public Gleitzeit getLatestGleitzeitByPersonalnummer(@PathVariable Integer personalnummer){
        return service.getLatestGleitzeitByPersonalnummer(personalnummer);
    }
}
