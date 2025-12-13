package com.joblink.model;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String accountType;

    public User() {
    }

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.accountType = "SEEKER";
    }

    public User(int id, String name, String email, String password, String accountType) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public boolean isEmployer() {
        return "EMPLOYER".equals(accountType);
    }

    public boolean isSeeker() {
        return "SEEKER".equals(accountType);
    }
    
    public boolean isAdmin() {
        return "ADMIN".equals(accountType);
    }
}
