package com.example.patientid_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientid_2.model.PatientHostoryModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class patienthistorylistAdapter extends RecyclerView.Adapter<patienthistorylistAdapter.ViewHolder> {
    Context applicationContext;
    ArrayList<PatientHostoryModel> listPatientsHistory;
    public patienthistorylistAdapter(Context applicationContext, ArrayList<PatientHostoryModel> listPatientsHistory) {
        this.listPatientsHistory=listPatientsHistory;
        this.applicationContext=applicationContext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(applicationContext).inflate(R.layout.customereportlist, parent, false);
        return new patienthistorylistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.date.setText(DateToString(listPatientsHistory.get(position).getDate_of_checkup()));
        String[] medicinInfo = listPatientsHistory.get(position).getMedicine_info().split("-");
        if (medicinInfo.length>1){
            try {
                for (int i = 0; i < medicinInfo.length ; i++) {
                    View view = LayoutInflater.from(applicationContext).inflate(R.layout.customview, null);
                    TextView medicinName = view.findViewById(R.id.medicinName);
                    TextView whentouse = view.findViewById(R.id.whentouse);
                    TextView dose = view.findViewById(R.id.dose);
                    TextView time = view.findViewById(R.id.time);
                    String[] medicine = medicinInfo[i].split("@");
                    medicinName.setText(medicine[0]);
                    String[] otherdata = medicine[1].split(";");
                    whentouse.setText(otherdata[0]);
                    dose.setText(otherdata[1]);
                    time.setText(otherdata[2]);
                    holder.viewlayout.addView(view);
                }
            } catch (Exception e){
            e.printStackTrace();
        }
        }else {
            try {
                View view = LayoutInflater.from(applicationContext).inflate(R.layout.customview, null);
                TextView medicinName = view.findViewById(R.id.medicinName);
                TextView whentouse = view.findViewById(R.id.whentouse);
                TextView dose = view.findViewById(R.id.dose);
                TextView time = view.findViewById(R.id.time);
                String[] medicine = medicinInfo[0].split("@");
                medicinName.setText(medicine[0]);
                String[] otherdata = medicine[1].split(";");
                whentouse.setText(otherdata[0]);
                dose.setText(otherdata[1]);
                time.setText(otherdata[2]);
                holder.viewlayout.addView(view);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
       /* holder.medicinName.setText(listPatientsHistory.get(position).get());
        holder.whentouse.setText(listPatientsHistory.get(position).getDate_of_checkup());
        holder.dose.setText(listPatientsHistory.get(position).getDate_of_checkup());
        holder.time.setText(listPatientsHistory.get(position).getDate_of_checkup());*/
        if (listPatientsHistory.get(position).getComplaint().equals("")) {
            holder.Complaint.setText("Not Mentioned");
        }else {
            holder.Complaint.setText(listPatientsHistory.get(position).getComplaint());
        }

        if (listPatientsHistory.get(position).getReport().equals("")) {
            holder.Reports.setText("Not Mentioned");
        }else {
            holder.Reports.setText(listPatientsHistory.get(position).getReport());
        }

    }

    public static String DateToString(String date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);// Set your date format
        Date date1 = null;
        try {
            date1 = originalFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String currentDate = dateFormat.format(date1); // Get Date String according to date format
        return currentDate;
    }

    @Override
    public int getItemCount() {
        return listPatientsHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView date,Complaint,Reports;//medicinName,whentouse,dose,time,
        LinearLayout viewlayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            viewlayout = itemView.findViewById(R.id.viewlayout);
           /* medicinName= itemView.findViewById(R.id.medicinName);
            whentouse= itemView.findViewById(R.id.whentouse);
            dose= itemView.findViewById(R.id.dose);
            time= itemView.findViewById(R.id.time);*/
            Complaint= itemView.findViewById(R.id.Complaint);
            Reports= itemView.findViewById(R.id.Reports);

        }
    }
}
