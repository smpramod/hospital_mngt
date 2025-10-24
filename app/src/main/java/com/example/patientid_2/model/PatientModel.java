package com.example.patientid_2.model;

public class PatientModel {

    String patient_name;
    String _id;
    String age;
    String mobileno;
    String gender;

    public String getPatientname() {
        return patient_name;
    }

    public String getPatient_id() {
        return _id;
    }

    public void setPatient_id(String patient_id) {
        this._id = patient_id;
    }

    public void setPatientname(String patientname) {
        this.patient_name = patientname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
