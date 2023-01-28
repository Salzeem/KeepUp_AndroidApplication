package com.example.android;

import java.time.LocalTime;
import java.util.Date;

public class Message {

    String message;
    LocalTime date;
    String sender;

    public Message(String message, LocalTime date, String sender)
    {

        this.message  = message;
        this.date = date;
        this.sender = sender;

    }

    public String getMessage()
    {
        return this.message;
    }

    public LocalTime getDate()
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

    public boolean setDate(LocalTime date)
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
