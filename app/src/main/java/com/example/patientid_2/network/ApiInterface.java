package com.example.patientid_2.network;
import com.example.patientid_2.model.ResponsePatient;
import com.example.patientid_2.model.ResponsePatienthistory;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST ("registerClinic.php")
    Call<ResponsePatient> saveClinicData(@Field("clinicname")String clinicname,
                                         @Field("Uname") String Uname,
                                         @Field("phone") String phone,
                                         @Field("email") String email,
                                         @Field("password")String password);


    @GET ("getLogin.php")
    Call<ResponsePatient> getLoginData(@Query("Uname") String patient_id,
                                         @Query("password") String report);

    @FormUrlEncoded
    @POST("RegisterClients.php")
    Call<ResponsePatient> savePatient(@Field("patientname") String patientname,
                                      @Field("age") String age,
                                      @Field("gender") String gender,
                                      @Field("mobno") String mobno);

    @GET("getpatients.php")
    Call<ResponsePatient> getPatient();

    @FormUrlEncoded
     @POST ("savepatient_data.php")
    Call<ResponsePatient> savePatientData(@Field("patient_id") String patient_id,
                                       @Field("medicine_info") String medicine_info,
                                       @Field("complaint") String complaint,
                                       @Field("report")String report);


    @FormUrlEncoded
    @POST ("getPatienthistory.php")
    Call<ResponsePatienthistory> getPatientHistory(@Field("patient_id")String pateint_id);
}

