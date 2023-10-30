package com.smabfws122a.humanressourcemanagement.service;

import com.smabfws122a.humanressourcemanagement.entity.Login;
import com.smabfws122a.humanressourcemanagement.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {
    @Autowired
     LoginRepository repository;

    public String addLogin(Login login){
        return repository.save(login).getEmail();
    }

    public List<Login> getAllLogin() {
        return repository.findAll();
    }

    public Login getLoginbyEmail(String email) {
        return repository.findById(email).orElseThrow(
                () -> new IllegalArgumentException("Could not find Login with email = " + email)
        );
    }

    public String updateLogin(Login login) {
        return repository.save(login).getEmail();
    }

    public void deleteLoginbyEmail(String email) {
        repository.deleteById(email);
    }

}
