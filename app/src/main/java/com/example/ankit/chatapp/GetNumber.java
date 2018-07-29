package com.example.ankit.chatapp;

/**
 * Created by ankit on 7/27/2018.
 */
public class GetNumber {
    private String Phone,Names;

    public GetNumber(String phone, String names) {
        Phone = phone;
        Names = names;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getNames() {
        return Names;
    }

    public void setNames(String names) {
        Names = names;
    }

    public GetNumber() {
    }

}
