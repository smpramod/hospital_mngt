package com.example.patientid_2.model;

import java.util.List;

public class ResponsePatienthistory {
    String message;
    String error_code;
    List<PatientHostoryModel> patients;

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

    public List<PatientHostoryModel> getPatients_history() {
        return patients;
    }

    public void setPatients_history(List<PatientHostoryModel> patients_history) {
        this.patients = patients_history;
    }
}
