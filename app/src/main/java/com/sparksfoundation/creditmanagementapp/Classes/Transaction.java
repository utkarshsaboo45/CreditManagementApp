package com.sparksfoundation.creditmanagementapp.Classes;

public class Transaction {

    int id;
    String sender;
    String receiver;
    String datetime;

    //constructor
    public Transaction()
    {

    }
    public Transaction(int id, String sender, String receiver, String datetime)
    {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.datetime = datetime;
    }

    //setters
    public void setId(int id)
    {
        this.id = id;
    }
    public void setSender(String sender)
    {
        this.sender = sender;
    }
    public void setReceiver(String receiver)
    {
        this.receiver = receiver;
    }
    public void setDatetime(String datetime)
    {
        this.datetime = datetime;
    }

    //getters
    public long getId()
    {
        return this.id;
    }
    public String getSender()
    {
        return this.sender;
    }
    public String getReceiver()
    {
        return this.receiver;
    }
    public String getDatetime()
    {
        return this.datetime;
    }
}
