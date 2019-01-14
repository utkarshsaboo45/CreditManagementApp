package com.sparksfoundation.creditmanagementapp.Classes;

public class User {

    int id;
    String name;
    String email;
    int currentCredits;

    //constructors
    public User()
    {

    }

    public User(int id, String name, String email, int currentCredits)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.currentCredits = currentCredits;
    }

    //setters
    public void setId(int id)
    {
        this.id = id;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public void setCurrentCredits(int currentCredits)
    {
        this.currentCredits = currentCredits;
    }

    //getters
    public long getId()
    {
        return this.id;
    }
    public String getName()
    {
        return this.name;
    }
    public String getEmail()
    {
        return this.email;
    }
    public int getCurrentCredits()
    {
        return this.currentCredits;
    }
}

