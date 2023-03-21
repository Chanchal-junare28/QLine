package com.ssgmc.qline;

public class AppointmentSchedule {
    private String uid, date;

    public AppointmentSchedule(String uid, String date) {
        this.uid = uid;
        this.date = date;
    }

    public AppointmentSchedule() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
