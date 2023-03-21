package com.ssgmc.qline;

public class User {
    public String firstname, lastname, email, pass, uid;
    public String appointmentDate = "NA";


    public User(){}

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public User(String firstname, String lastname, String email, String pass, String uid, String date) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.pass = pass;
        this.uid = uid;
        this.appointmentDate = date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
