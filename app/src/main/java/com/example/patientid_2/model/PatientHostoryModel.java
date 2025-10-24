package com.example.patientid_2.model;

public class PatientHostoryModel {

    String  _id;
    String  patient_id;
    String  medicine_info;
    String  complaint;
    String  report;
    String  date_of_checkup;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getMedicine_info() {
        return medicine_info;
    }

    public void setMedicine_info(String medicine_info) {
        this.medicine_info = medicine_info;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getDate_of_checkup() {
        return date_of_checkup;
    }

    public void setDate_of_checkup(String date_of_checkup) {
        this.date_of_checkup = date_of_checkup;
    }
}
