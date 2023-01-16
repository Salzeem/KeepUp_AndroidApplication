package com.example.android;

import java.util.Date;

public class Message {

    String message;
    Date date;
    String sender;

    public Message(String message, Date date, String sender)
    {

        this.message  = message;
        this.date = date;
        this.sender = sender;

    }

    public String getMessage()
    {
        return this.message;
    }

    public Date getDate()
    {
        return this.date;
    }

    public String getSender()
    {
        return this.sender;
    }


    public boolean setMessage(String message)
    {
        this.message = message;
        return true;
    }

    public boolean setDate(Date date)
    {
        this.date = date;
        return true;
    }

    public boolean setsender(String sender)
    {
        this.sender = sender;
        return true;
    }

}
