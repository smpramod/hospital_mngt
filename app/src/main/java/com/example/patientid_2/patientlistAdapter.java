package com.example.patientid_2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.patientid_2.model.PatientModel;

import java.util.ArrayList;

public class patientlistAdapter extends RecyclerView.Adapter<patientlistAdapter.ViewHOlder> {

    Context applicationContext;
    ArrayList<PatientModel> listPatients;
    onClickListener  onclicklistner;
    ArrayList<PatientModel> dataSet = new ArrayList<>();;
    public patientlistAdapter(Context applicationContext, ArrayList<PatientModel> listPatients, ArrayList<PatientModel> templistPatients, onClickListener  onclicklistner) {
        this.listPatients=listPatients;
        this.applicationContext=applicationContext;
        dataSet = templistPatients;
        this.onclicklistner=onclicklistner;
    }

    interface  onClickListener{
        public void onClick(PatientModel model);
    }

    @NonNull
    @Override
    public ViewHOlder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(applicationContext).inflate(R.layout.patientlist, parent, false);
        return new patientlistAdapter.ViewHOlder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHOlder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(dataSet.get(position).getPatientname());
        holder.Mobileno.setText(dataSet.get(position).getMobileno());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclicklistner.onClick(dataSet.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class ViewHOlder extends RecyclerView.ViewHolder{
        TextView name,Mobileno;
        public ViewHOlder(@NonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.name);
            Mobileno = itemView.findViewById(R.id.Mobileno);
        }
    }


    public Filter getSearched_Filter() {
        return Searched_Filter;
    }
    private Filter Searched_Filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<PatientModel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listPatients);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (PatientModel item : listPatients) {
                    if (item.getPatientname().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try {
                dataSet.clear();
                dataSet.addAll((ArrayList) results.values);
                notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
}
