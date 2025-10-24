package com.example.patientid_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patientid_2.model.ResponsePatient;
import com.example.patientid_2.network.ApiClient;
import com.example.patientid_2.network.ApiInterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sign_Up extends AppCompatActivity {

   // DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginsignup-41e2a-default-rtdb.firebaseio.com");
    EditText clinicname, Uname, phone, email, password, ConfirmPassword;
    int pass_LENGTH = 6;
    int ph_len1=11;
    int ph_len2=0;

    public static PrefrenceManager prefrenceManager;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        clinicname = findViewById(R.id.clinicname);
        Uname=findViewById(R.id.Uname);
        phone=findViewById(R.id.phone);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        ConfirmPassword=findViewById(R.id.CPassword);
        prefrenceManager = new PrefrenceManager(Sign_Up.this);

        TextView login_in= findViewById(R.id.login_in);

        login_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Sign_Up.this,MainActivity.class));
                finish();
            }
        });

    }


    boolean validate() {
        if (Uname.getText().toString().equals("")) {
            Uname.setError("Please Enter First Name");
            return false;
        }
        if (phone.getText().toString().equals("")) {
            phone.setError("Please Enter Last Name");
            return false;
        }
        if (email.getText().toString().equals("")) {
            email.setError("Please Enter Email");
            return false;
        }
        if (password.getText().toString().equals("")) {
            password.setError("Please Enter Password");
            return false;
        }
        if (ConfirmPassword.getText().toString().equals("")) {
            ConfirmPassword.setError("Please Confirm Password");
            return false;
        }


        if (!isEmailValid(email.getText().toString())) {
            email.setError("Please Enter Valid Email");
            return false;
        }


        if (password.getText().length() < pass_LENGTH) {
            password.setError("Password Length must be more than " + pass_LENGTH + "characters");
            return false;
        }

        if (phone.getText().toString().equals("")) {
            phone.setError("Password Length must be more than " + pass_LENGTH + "characters");
            return false;
        }


        if (!password.getText().toString().equals(ConfirmPassword.getText().toString())) {
            ConfirmPassword.setError("Password does not match");
            return false;
        }
        return true;
    }

    boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void CreateAccount(View view) {
        String clinic_name = clinicname.getText().toString();
        String firstName = Uname.getText().toString();
        String Phone = phone.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String repeatPassword = ConfirmPassword.getText().toString();

        if (validate()) {
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<ResponsePatient> call = apiInterface.saveClinicData(clinic_name,firstName,Phone,Email,Password);
            call.enqueue(new Callback<ResponsePatient>() {
                @Override
                public void onResponse(Call<ResponsePatient> call, Response<ResponsePatient> response) {
                    if (response.isSuccessful()){
                        if (response.body().getError_code().equals("100")){
                            Toast.makeText(Sign_Up.this,"Sign Up Success",Toast.LENGTH_SHORT).show();
                            prefrenceManager.setUsername("firstName");
                            prefrenceManager.setPassword("Password");
                            startActivity(new Intent(Sign_Up.this,Home_Page.class));
                            finish();
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponsePatient> call, Throwable t) {
                    t.printStackTrace();
                }
            });
            /*databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(Phone))
                    {
                        Toast.makeText(Sign_Up.this,"Phone is already registered",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                        databaseReference.child("users").child(Phone).child("UName").setValue(firstName);
                        databaseReference.child("users").child(Phone).child("email").setValue(Email);
                        databaseReference.child("users").child(Phone).child("password").setValue(Password);
                        Toast.makeText(Sign_Up.this,"Sign Up Success",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Sign_Up.this,Home_Page.class));
                        finish();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    error.toString();
                }
            });*/
        }
        else
        {
            Toast.makeText(this,"Sign Up Failed",Toast.LENGTH_SHORT).show();

        }

    }
}