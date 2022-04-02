package com.example.calculator.dto;

public class ActionData {
    public String data;
    public int position;
    public int priority;

    public ActionData(String data, int position, int priority)
    {
        this.data = data;
        this.position = position;
        this.priority = priority;

    }

    public int getPriority()
    {
        return this.priority;
    }
}
