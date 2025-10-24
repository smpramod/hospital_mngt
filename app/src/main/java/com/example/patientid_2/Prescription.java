package com.example.patientid_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.example.patientid_2.model.PatientHostoryModel;
import com.example.patientid_2.model.PatientModel;
import com.example.patientid_2.model.ResponsePatient;
import com.example.patientid_2.model.ResponsePatienthistory;
import com.example.patientid_2.network.ApiClient;
import com.example.patientid_2.network.ApiInterface;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Prescription extends AppCompatActivity implements patientlistAdapter.onClickListener{
    Button search_patient;
    TextView patientname;
    Dialog dialog;
    private String pateint_id="";

    ArrayList<PatientModel> listPatients;
    ArrayList<PatientModel> TemplistPatients;
    ArrayList<PatientHostoryModel> listPatientsHistory;
    patienthistorylistAdapter adapterPatientHiatorylist;
    patientlistAdapter adapterPatientlist;
    RecyclerView reports;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        patientname = findViewById(R.id.patient_name);
        search_patient = findViewById(R.id.search_history_patient);
        reports = findViewById(R.id.reports);
        reports.setLayoutManager(new LinearLayoutManager(this));

        listPatients = new ArrayList<>();
        TemplistPatients = new ArrayList<>();
        listPatientsHistory = new ArrayList<>();

        adapterPatientlist = new patientlistAdapter(getApplicationContext(),listPatients,TemplistPatients,this);
        adapterPatientHiatorylist = new patienthistorylistAdapter(getApplicationContext(),listPatientsHistory);
        reports.setAdapter(adapterPatientHiatorylist);

        View view1=getLayoutInflater().inflate(R.layout.listview_patient,null);
        dialog = new Dialog(Prescription.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view1);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RecyclerView recyclerView = dialog.findViewById(R.id.pateintlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(Prescription.this));
        recyclerView.setAdapter(adapterPatientlist);

        search_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();
                TextInputEditText search = dialog.findViewById(R.id.search);
                search.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        String s =editable.toString();
                        adapterPatientlist.getSearched_Filter().filter(s);
                    }
                });
            }
        });
        loadPerson();

    }


    public void back(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        finish();
    }

    private void loadPerson() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ResponsePatient> call = apiInterface.getPatient();
        call.enqueue(new Callback<ResponsePatient>() {
            @Override
            public void onResponse(Call<ResponsePatient> call, Response<ResponsePatient> response) {
                if (response.isSuccessful()){
                    if (response.body().getPatients().size()>0){
                        listPatients.clear();
                        TemplistPatients.clear();
                        listPatients.addAll(response.body().getPatients());
                        TemplistPatients = response.body().getPatients();
                        adapterPatientlist.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponsePatient> call, Throwable t) {

            }
        });
    }

    private void loadPatientData(String pateint_id) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ResponsePatienthistory> call = apiInterface.getPatientHistory(pateint_id);
        call.enqueue(new Callback<ResponsePatienthistory>() {
            @Override
            public void onResponse(Call<ResponsePatienthistory> call, Response<ResponsePatienthistory> response) {
                if (response.isSuccessful()){
                    if (response.body().getPatients_history().size()>0){
                        listPatientsHistory.clear();
                        listPatientsHistory.addAll(response.body().getPatients_history());
                        adapterPatientHiatorylist.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponsePatienthistory> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(PatientModel model) {
        patientname.setText(model.getPatientname());
        pateint_id = model.getPatient_id();
        dialog.dismiss();
        loadPatientData(pateint_id);
    }

/**/
}