package com.ssgmc.qline;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.HashMap;
public class AppointmentDates {
//    private String date;
//    private int noOfAppointment;
    private HashMap<String, Integer> dateAp;

    public AppointmentDates(HashMap<String, Integer> dateAp) {
        this.dateAp = dateAp;
    }

    public HashMap<String, Integer> getDateAp() {
        return dateAp;
    }

    public void setDateAp(HashMap<String, Integer> dateAp) {
        this.dateAp = dateAp;
    }

    public AppointmentDates() {
    }
//
//    public AppointmentDates(String date, int noOfAppointment) {
//        this.date = date;
//        this.noOfAppointment = noOfAppointment;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }
//
//    public int getNoOfAppointment() {
//        return noOfAppointment;
//    }
//
//    public void setNoOfAppointment(int noOfAppointment) {
//        this.noOfAppointment = noOfAppointment;
//    }
}
