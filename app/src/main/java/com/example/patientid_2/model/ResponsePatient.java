package com.example.patientid_2.model;

import java.util.ArrayList;
import java.util.List;

public class ResponsePatient {
    String message;
    String error_code;
    ArrayList<PatientModel> patients;


    public ArrayList<PatientModel> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<PatientModel> patients) {
        this.patients = patients;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }
}
