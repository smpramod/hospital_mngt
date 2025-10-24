package com.example.patientid_2.model;

public class MedicineModel {
    String MedicineName;
    String whentotake;
    String dose;
    String time;

    public MedicineModel(String medicineName, String whentotake, String dose, String time) {
        MedicineName = medicineName;
        this.whentotake = whentotake;
        this.dose = dose;
        this.time = time;
    }

    public String getMedicineName() {
        return MedicineName;
    }

    public void setMedicineName(String medicineName) {
        MedicineName = medicineName;
    }

    public String getWhentotake() {
        return whentotake;
    }

    public void setWhentotake(String whentotake) {
        this.whentotake = whentotake;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
