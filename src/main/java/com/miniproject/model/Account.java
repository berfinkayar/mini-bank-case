package com.miniproject.model;
import jakarta.persistence.*;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String currency;
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    private String password;

    // Getters and setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getCurrency() {return currency;}
    public void setCurrency(String currency) {this.currency = currency;}
    public BigDecimal getBalance() {return balance;}
    public void setBalance(BigDecimal balance) {this.balance = balance;}
    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
}
