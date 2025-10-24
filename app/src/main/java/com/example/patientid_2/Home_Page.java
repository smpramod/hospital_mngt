package com.example.patientid_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
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

public class Home_Page extends AppCompatActivity {

    //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginsignup-41e2a-default-rtdb.firebaseio.com");
    TextView sign_up;
    EditText password1,Username1;
    AppCompatButton log_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);


        sign_up=findViewById(R.id.sign_up);
        log_Button=findViewById(R.id.log_button);
        password1=findViewById(R.id.password1);
        Username1=findViewById(R.id.username1);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home_Page.this,Sign_Up.class));
                finish();
            }
        });

    }

    public void log_in(View view) {
       String Password1 = password1.getText().toString();
       String Username1Txt = Username1.getText().toString();

        if (password1.getText().toString().equals("") )
        {
            Toast.makeText(this,"Please Enter Data",Toast.LENGTH_SHORT).show();

        }else{

            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<ResponsePatient> call = apiInterface.getLoginData(Username1Txt,Password1);
            call.enqueue(new Callback<ResponsePatient>() {
                @Override
                public void onResponse(Call<ResponsePatient> call, Response<ResponsePatient> response) {
                    if (response.isSuccessful()){
                        if (response.body().getError_code().equals("100")){
                            Toast.makeText(Home_Page.this,"Sign Up Success",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Home_Page.this,dashboard.class));
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


                    if (snapshot.hasChild(phone1Txt))
                    {
                        String getPassword = snapshot.child(phone1Txt).child("password").getValue(String.class);
                        String getUsername = snapshot.child(Username1Txt).child("UName").getValue(String.class);

                        if (getPassword.equals(Password1) || getUsername.equals(Username1Txt))
                        {
                            Toast.makeText(Home_Page.this,"Login Successful",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Home_Page.this, dashboard.class));
                            finish();
                        }

                        else
                        {
                            Toast.makeText(Home_Page.this,"Wrong Password",Toast.LENGTH_SHORT).show();
                        }
                    }

                    else
                    {
                        Toast.makeText(Home_Page.this,"You Don't Have Account",Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });*/

        }
    }
}