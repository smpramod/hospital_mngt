package com.example.patientid_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.patientid_2.model.ResponsePatient;
import com.example.patientid_2.network.ApiClient;
import com.example.patientid_2.network.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class patientactivty extends AppCompatActivity {

    EditText patient_name, mobielno, age;
    RadioButton male, female, other;
    Button savepatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientactivty);

        patient_name = findViewById(R.id.patient_name);
        mobielno = findViewById(R.id.mobielno);
        age = findViewById(R.id.age);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        other = findViewById(R.id.other);

        savepatient = findViewById(R.id.savepatient);

        savepatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gender = "";
                if (male.isChecked()){
                    gender="male";
                } else if (female.isChecked()) {
                    gender="female";
                } else if (other.isChecked()) {
                    gender="other";
                }

                if (TextUtils.isEmpty(patient_name.getText().toString())) {
                    Toast.makeText(patientactivty.this, "Please enter patient name here ", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(mobielno.getText().toString())) {
                    Toast.makeText(patientactivty.this, "Please eeter mobile number", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(age.getText().toString())) {
                    Toast.makeText(patientactivty.this, "Please enter patient age", Toast.LENGTH_SHORT).show();
                    return;
                } else if (gender.equals("")) {
                    Toast.makeText(patientactivty.this, "Please select gender", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    savePatient(patient_name.getText().toString(),mobielno.getText().toString(),age.getText().toString(),gender);
                }
            }
        });
    }

    private void savePatient(String patientname, String mobileno, String age, String gender) {
        ApiInterface api = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ResponsePatient> call = api.savePatient(patientname,age,gender,mobileno);
        call.enqueue(new Callback<ResponsePatient>() {
            @Override
            public void onResponse(Call<ResponsePatient> call, Response<ResponsePatient> response) {
                if (response.isSuccessful()){
                    if (response.body().getError_code().equals("100")){
                        Toast.makeText(patientactivty.this, "Patient added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(patientactivty.this,MainActivity.class));
                    }else {
                        Toast.makeText(patientactivty.this, "Oooppss!.....Patient can not added", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePatient> call, Throwable t) {
                Toast.makeText(patientactivty.this, "Something wrong, please try after some time", Toast.LENGTH_SHORT).show();
            }
        });
    }
}