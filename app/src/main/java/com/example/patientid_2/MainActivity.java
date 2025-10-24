package com.example.patientid_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.patientid_2.model.MedicineModel;
import com.example.patientid_2.model.PatientModel;
import com.example.patientid_2.model.ResponsePatient;
import com.example.patientid_2.network.ApiClient;
import com.example.patientid_2.network.ApiInterface;
import com.google.android.material.textfield.TextInputEditText;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements patientlistAdapter.onClickListener {

    Button search_patient, savepatientcheckupdata, savepatientcheckupdatawithpdf;
    RecyclerView medicine;
    TextView patientname;
    EditText complaint, reports;
    ArrayList<MedicineModel> listMedicine;
    ArrayList<PatientModel> TemplistPatients;
    ArrayList<PatientModel> listPatients;
    MedicineAdapter adapter;
    patientlistAdapter adapterPatientlist;
    Dialog dialog;
    private String pateint_id = "";
    ProgressBar progress;
    private Image imgReportLogo;
    BaseColor headColor = WebColors.getRGBColor("#DEDEDE");
    BaseColor tableHeadColor = WebColors.getRGBColor("#99CCFF");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_patient = findViewById(R.id.search_patient);
        savepatientcheckupdata = findViewById(R.id.savepatientcheckupdata);
        savepatientcheckupdatawithpdf = findViewById(R.id.savepatientcheckupdatawithpdf);
        patientname = findViewById(R.id.patientname);
        complaint = findViewById(R.id.complaint);
        reports = findViewById(R.id.reports);
        medicine = findViewById(R.id.medicine);
        progress = findViewById(R.id.progress);
        listMedicine = new ArrayList<>();
        listPatients = new ArrayList<>();
        TemplistPatients = new ArrayList<>();
        medicine.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MedicineAdapter(getApplicationContext(), listMedicine);
        adapterPatientlist = new patientlistAdapter(getApplicationContext(), listPatients, TemplistPatients, this);
        medicine.setAdapter(adapter);
        TextView history = findViewById(R.id.history);
        //loading = new KProgressHUD(this);

        View view1 = getLayoutInflater().inflate(R.layout.listview_patient, null);
        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view1);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        RecyclerView recyclerView = dialog.findViewById(R.id.pateintlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(adapterPatientlist);
        loadPerson();

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
                        String s = editable.toString();
                        adapterPatientlist.getSearched_Filter().filter(s);
                    }
                });
            }
        });

        savepatientcheckupdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(patientname.getText().toString())) {
                    Toast.makeText(MainActivity.this, "please select patient", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (listMedicine.size() == 0) {
                    Toast.makeText(MainActivity.this, "Please add some medicine", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveData("", pateint_id, listMedicine, complaint.getText().toString(), reports.getText().toString());
            }
        });
        savepatientcheckupdatawithpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(patientname.getText().toString())) {
                    Toast.makeText(MainActivity.this, "please select patient", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (listMedicine.size() == 0) {
                    Toast.makeText(MainActivity.this, "Please add some medicine", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveData("generate", pateint_id, listMedicine, complaint.getText().toString(), reports.getText().toString());
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Prescription.class));
                finish();
            }
        });
    }

    private void saveData(String saveFor, String pateint_id, ArrayList<MedicineModel> listMedicine, String complaintss, String reportsss) {
        progress.setVisibility(View.VISIBLE);
        savepatientcheckupdata.setClickable(false);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        String medicineinfo = "";
        for (int i = 0; i < listMedicine.size(); i++) {
            medicineinfo = medicineinfo + listMedicine.get(i).getMedicineName() + "@" + listMedicine.get(i).getWhentotake() + ";" + listMedicine.get(i).getDose() + ";" + listMedicine.get(i).getTime() + "-";
        }
        Call<ResponsePatient> call = apiInterface.savePatientData(pateint_id, medicineinfo, complaintss, reportsss);
        String finalMedicineinfo = medicineinfo;
        call.enqueue(new Callback<ResponsePatient>() {
            @Override
            public void onResponse(Call<ResponsePatient> call, Response<ResponsePatient> response) {
                if (response.isSuccessful()) {
                    progress.setVisibility(View.GONE);
                    savepatientcheckupdata.setClickable(true);
                    if (response.body().getError_code().equals("100")) {
                        Toast.makeText(MainActivity.this, "Patient data saved sucessfully", Toast.LENGTH_SHORT).show();
                        listMedicine.clear();
                        complaint.setText("");
                        reports.setText("");
                        adapter.notifyDataSetChanged();
                        if (saveFor.equals("generate")) {
                            try {
                                loadPDF(finalMedicineinfo);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            } catch (DocumentException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePatient> call, Throwable t) {
                progress.setVisibility(View.GONE);
                savepatientcheckupdata.setClickable(true);
                t.printStackTrace();
            }
        });
    }

    private void loadPDF(String finalMedicineinfo) throws FileNotFoundException, DocumentException {
//Create Pdf File
        {
            //listfiles = new ArrayList<>();
            /*Pdf redesigned by Deepali on 09.12.2022*/
            PdfPCell cell;
            try {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmm");
                String path = Environment.getExternalStorageDirectory().getAbsolutePath();

                File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "");
                if (!folder.exists())
                    folder.mkdirs();
                else if (!folder.isDirectory() && folder.canWrite()) {
                    folder.delete();
                    folder.mkdirs();
                }
                String fileName = "";
                Document document = new Document();
                // Location to save
                fileName = "PatientPDF_" + dateFormat.format(Calendar.getInstance().getTime()) + ".pdf";
                Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
                        Font.BOLD);
                Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
                        Font.NORMAL);
                Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
                        Font.BOLD);
                try {
                    File filecheck = new File(folder, fileName);
                    filecheck.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(filecheck, false);

                    PdfWriter.getInstance(document, fOut);

                } catch (DocumentException e) {
                    e.printStackTrace();
                    Log.v("PdfError", e.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.v("PdfError", e.toString());


                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Open the document
                document.open();
                try {
                    try {
                        //Set Logo in Header Cell985*/88/
                        try {
                            PdfPTable header = new PdfPTable(1);
                            header.setWidthPercentage(100);
                            cell = new PdfPCell();
                            cell.setHorizontalAlignment(1);
                            cell.setVerticalAlignment(1);
                            cell.setPaddingLeft(175.0f);
                            //cell.setBackgroundColor(headColor);
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.addElement(new Paragraph("Gurudatta Clinic",catFont));
                            header.addCell(cell);

                            cell = new PdfPCell();
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.addElement(new Paragraph("Date: " + format.format(Calendar.getInstance().getTime()), redFont));
                            cell.setHorizontalAlignment(1);
                            cell.setVerticalAlignment(1);
                            cell.setPaddingLeft(185.0f);
                            header.addCell(cell);

                            cell = new PdfPCell();
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.setPaddingLeft(85.0f);
                            cell.addElement(new Paragraph("Dr. R.R. Shejal(B.H.M.S)" + "                          " + "Phone No. : 7798009898", redFont));
                            header.addCell(cell);

                            cell = new PdfPCell();
                            cell.setBorder(Rectangle.NO_BORDER);
                            cell.addElement(new Paragraph(""));
                            header.addCell(cell);

                            document.add(header);

                            PdfPTable table;

                            cell = new PdfPCell(new Phrase(" "));
                            cell.setBackgroundColor(headColor);

                            table = new PdfPTable(5);
                            table.setWidthPercentage(100.0f);

                            cell = new PdfPCell(new Phrase("Sr.No."));
                            //cell.setBackgroundColor(tableHeadColor);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("Medicine Name"));
                            cell.setPadding(3.0f);
                            //cell.setBackgroundColor(tableHeadColor);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("Dosage"));
                            cell.setPadding(3.0f);
                            //cell.setBackgroundColor(tableHeadColor);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("When to take"));
                            cell.setPadding(3.0f);
                            //cell.setBackgroundColor(tableHeadColor);
                            table.addCell(cell);

                            cell = new PdfPCell(new Phrase("Time"));
                            cell.setPadding(3.0f);
                            //cell.setBackgroundColor(tableHeadColor);
                            table.addCell(cell);

                            String[] medicinInfo = finalMedicineinfo.split("-");

                            for (int i = 0; i < medicinInfo.length; i++) {

                                String[] medicine = medicinInfo[i].split("@");
                                cell.setPadding(3.0f);
                                table.addCell(String.valueOf(i + 1));
                                table.addCell(medicine[0]);
                                String[] otherdata = medicine[1].split(";");
                                table.addCell(otherdata[1]);
                                if (otherdata[0].equals("M")) {
                                    table.addCell("Moring");
                                } else if (otherdata[0].equals("A")) {
                                    table.addCell("AfterNoon");
                                } else if (otherdata[0].equals("N")) {
                                    table.addCell("Night");
                                } else {
                                    table.addCell(otherdata[0]);
                                }

                                if (otherdata[2].equals("A")) {
                                    table.addCell("After meal");
                                } else {
                                    table.addCell("Before meal");
                                }

                            }
                            document.add(table);
                        } catch (DocumentException de) {
                            Log.e("PDFCreator", "DocumentException:" + de);
                        }
                    } catch (Exception e) {
                        Log.e("PDFCreator", "DocumentException:" + e);
                    }

                } finally {
                    document.close();
                }
            } catch (
                    Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadPerson() {
        progress.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ResponsePatient> call = apiInterface.getPatient();
        call.enqueue(new Callback<ResponsePatient>() {
            @Override
            public void onResponse(Call<ResponsePatient> call, Response<ResponsePatient> response) {
                if (response.isSuccessful()) {
                    progress.setVisibility(View.GONE);
                    if (response.body().getPatients().size() > 0) {
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
                progress.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    public void show(View view) {
        /*startActivity(new Intent(MainActivity.this,Prescription.class));
        finish();*/
        View view1 = getLayoutInflater().inflate(R.layout.med2, null);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view1);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

        Button search_medicine = dialog.findViewById(R.id.search_medicine);
        Button done = dialog.findViewById(R.id.ok);
        AutoCompleteTextView medicine = dialog.findViewById(R.id.a1);

        final String[] checkM = {""};
        final String[] checkA = {""};
        final String[] checkN = {""};

        CheckBox morning = dialog.findViewById(R.id.M);
        morning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkM[0] = "M";
                } else {
                    checkM[0] = "";
                }
            }
        });
        CheckBox afternoon = dialog.findViewById(R.id.A);
        afternoon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkA[0] = "A";
                } else {
                    checkA[0] = "";
                }
            }
        });
        CheckBox noon = dialog.findViewById(R.id.N);
        noon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkN[0] = "N";
                } else {
                    checkN[0] = "";
                }
            }
        });

        final String[] checkOne = {""};
        final String[] checkHalf = {""};
        final String[] checkTwo = {""};

        CheckBox one = dialog.findViewById(R.id.one);
        one.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkOne[0] = "1";
                } else {
                    checkOne[0] = "";
                }
            }
        });
        CheckBox half = dialog.findViewById(R.id.half);
        half.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkHalf[0] = "1/2";
                } else {
                    checkHalf[0] = "";
                }
            }
        });
        CheckBox two = dialog.findViewById(R.id.two);
        two.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkTwo[0] = "2";
                } else {
                    checkTwo[0] = "";
                }
            }
        });

        final String[] checkAfterMeal = {""};
        final String[] checkBeforeMeal = {""};

        CheckBox aftermeal = dialog.findViewById(R.id.aftermeal);
        aftermeal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkAfterMeal[0] = "A";
                } else {
                    checkAfterMeal[0] = "";
                }
            }
        });
        CheckBox beforemeal = dialog.findViewById(R.id.beforemeal);
        beforemeal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkBeforeMeal[0] = "B";
                } else {
                    checkBeforeMeal[0] = "";
                }
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(medicine.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please add medicine", Toast.LENGTH_SHORT).show();
                    return;
                } else if (checkM[0].equals("") && checkA[0].equals("") && checkN[0].equals("")) {
                    Toast.makeText(MainActivity.this, "Please select atleast one time", Toast.LENGTH_SHORT).show();
                    return;
                } else if (checkOne[0].equals("") && checkTwo[0].equals("") && checkHalf[0].equals("")) {
                    Toast.makeText(MainActivity.this, "Please select atleast one quantity", Toast.LENGTH_SHORT).show();
                    return;
                } else if (checkAfterMeal[0].equals("") && checkBeforeMeal[0].equals("")) {
                    Toast.makeText(MainActivity.this, "Please select atleast one meal", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String whentouse = "";
                    if (!checkM[0].equals("")) {
                        whentouse = checkM[0] + ",";
                    }
                    if (!checkA[0].equals("")) {
                        whentouse = whentouse + checkA[0] + ",";
                    }
                    if (!checkN[0].equals("")) {
                        whentouse = whentouse + checkN[0];
                    }

                    String dose = "";
                    if (!checkOne[0].equals("")) {
                        dose = checkOne[0] + ",";
                    }
                    if (!checkHalf[0].equals("")) {
                        dose = dose + checkHalf[0] + ",";
                    }
                    if (!checkTwo[0].equals("")) {
                        dose = dose + checkTwo[0];
                    }

                    String time = "";
                    if (!checkAfterMeal[0].equals("")) {
                        time = checkAfterMeal[0] + ",";
                    }
                    if (!checkBeforeMeal[0].equals("")) {
                        time = time + checkBeforeMeal[0];
                    }

                    if (whentouse.length() > 1) {
                        String str = whentouse;
                        if (str.substring(str.length() - 1).equals(",")) {
                            whentouse = whentouse.substring(0, whentouse.length() - 1);
                        }
                    }
                    if (dose.length() > 1) {
                        String str = dose;
                        if (str.substring(str.length() - 1).equals(",")) {
                            dose = dose.substring(0, dose.length() - 1);
                        }
                    }
                    if (time.length() > 1) {
                        String str = time;
                        if (str.substring(str.length() - 1).equals(",")) {
                            time = time.substring(0, time.length() - 1);
                        }
                    }
                    MedicineModel model = new MedicineModel(medicine.getText().toString(), whentouse, dose, time);
                    listMedicine.add(model);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });

    }

    @Override
    public void onClick(PatientModel model) {
        patientname.setText(model.getPatientname());
        pateint_id = model.getPatient_id();
        dialog.dismiss();
    }
}