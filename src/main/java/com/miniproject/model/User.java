package com.miniproject.model;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class User {

    @Id
    @Column(unique = true, nullable = false, updatable = false)
    private final Long id;

    private String name;
    private String surname;
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Account> accounts = new ArrayList<>();

    protected User(){
        this.id = null;
    }

    public User(Long id, String name, String surname, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public Long getId() {return id;}
    public String getName() {return name;}
    public void setName(String newName) {name = newName;}
    public String getSurname() {return surname;}
    public void setSurname(String newSurname) {surname = newSurname;}
    public String getEmail() {return email;}
    public void setEmail(String newEmail) {email = newEmail;}
    public List<Account> getAccounts() {return accounts;}


}
