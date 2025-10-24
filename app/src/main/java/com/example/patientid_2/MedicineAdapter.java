package com.example.patientid_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientid_2.model.MedicineModel;

import java.util.ArrayList;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.viewholder> {

    Context applicationContext;
    ArrayList<MedicineModel> listMedicine;
    public MedicineAdapter(Context applicationContext, ArrayList<MedicineModel> listMedicine) {
        this.applicationContext=applicationContext;
        this.listMedicine=listMedicine;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(applicationContext).inflate(R.layout.custom_medicine_view, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.medicinename.setText(listMedicine.get(position).getMedicineName());
        holder.whentouse.setText(listMedicine.get(position).getWhentotake());
        holder.time.setText(listMedicine.get(position).getTime());
        holder.dose.setText(listMedicine.get(position).getDose());
    }

    @Override
    public int getItemCount() {
        return listMedicine.size();
    }


    public class viewholder extends RecyclerView.ViewHolder{

        TextView medicinename,whentouse,dose,time;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            medicinename = itemView.findViewById(R.id.medicinName);
            whentouse = itemView.findViewById(R.id.whentouse);
            dose = itemView.findViewById(R.id.dose);
            time = itemView.findViewById(R.id.time);

        }
    }

}
