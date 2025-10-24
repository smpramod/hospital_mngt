package com.example.patientid_2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.patientid_2.model.ItemObject;

import java.util.List;

/**
 * Created by rppatil on 13/12/2017.
 */

public class CustomAdapter extends BaseAdapter {
    private LayoutInflater layoutinflater;
    private List<ItemObject> listStorage;
    private Context context;

    public CustomAdapter(Context context, List<ItemObject> customizedListView) {
        this.context = context;
        layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final String act;
        ViewHolder listViewHolder;
        if (convertView == null) {
            listViewHolder = new ViewHolder();

            convertView = layoutinflater.inflate(R.layout.card_layout, parent, false);
            listViewHolder.screenShot = (ImageView) convertView.findViewById(R.id.screen_shot);
            listViewHolder.musicName = (TextView) convertView.findViewById(R.id.music_name);
            convertView.setTag(listViewHolder);

        } else {
            listViewHolder = (ViewHolder) convertView.getTag();
        }
        listViewHolder.screenShot.setImageResource(listStorage.get(position).getScreenShot());
        listViewHolder.musicName.setText(listStorage.get(position).getMusicName());

        act = listStorage.get(position).getMusicAuthor();
        final View.OnClickListener imgListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i;

                if (act == "Patient master") {
                    i = new Intent(context, patientactivty.class);
                    context.startActivity(i);
                }
                if (act == "Check Up") {
                    i = new Intent(context, MainActivity.class);
                    context.startActivity(i);
                }
                if (act=="History"){
                    i = new Intent(context, Prescription.class);
                    context.startActivity(i);
                }
            }

        };

        listViewHolder.screenShot.setOnClickListener(imgListener);
        return convertView;
    }

    static class ViewHolder {
        public CardView card_view;
        ImageView screenShot;
        TextView musicName;
        TextView musicAuthor;
    }
}