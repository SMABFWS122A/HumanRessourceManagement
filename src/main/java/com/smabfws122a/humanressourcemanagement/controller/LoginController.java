package com.smabfws122a.humanressourcemanagement.controller;

import com.smabfws122a.humanressourcemanagement.entity.Login;
import com.smabfws122a.humanressourcemanagement.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    LoginService service;

    @PostMapping(value = "/login")
    public String addLogin(@RequestBody Login login){
        return service.addLogin(login);
    }

    @GetMapping(value = "/login")
    public List<Login> getAllLogin(){
        return service.getAllLogin();
    }

    @GetMapping(value = "/login/{email}")
    public Login getLoginbyEmail(@PathVariable String email){
        return service.getLoginbyEmail(email).orElseThrow(
                ()-> new ResponseStatusException( HttpStatus.NOT_FOUND, "entity not found")
        );
    }

    @PutMapping(value = "/login")
    public String updateLogin(@RequestBody Login login){
        return service.updateLogin(login);
    }

    @DeleteMapping(value = "/login/{email}")
    public void deleteLoginbyEmail(@PathVariable String email){
        service.deleteLoginbyEmail(email);
    }
}

