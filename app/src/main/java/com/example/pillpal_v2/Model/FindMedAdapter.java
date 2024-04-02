package com.example.pillpal_v2.Model;



import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pillpal_v2.R;

import java.util.ArrayList;
import java.util.List;

public class FindMedAdapter extends RecyclerView.Adapter<FindMedAdapter.ViewHolder> {
    private Context context;
    private ArrayList<FindMedConstructor> list;



    public FindMedAdapter(Context context, ArrayList<FindMedConstructor> list) {
        this.context=context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView priceTextView;
        public TextView location;
        TextView phone;
        ImageView call;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.find_med_pharmacy_name);
            priceTextView = itemView.findViewById(R.id.find_med_price);
            location = itemView.findViewById(R.id.find_med_location_place);
            phone=itemView.findViewById(R.id.find_med_location_phone);
        call=itemView.findViewById(R.id.callnumber);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_med_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FindMedConstructor pharmacy = list.get(position);
        holder.nameTextView.setText(pharmacy.getPharmaName());
        holder.priceTextView.setText(pharmacy.getPrice());
        holder.location.setText(pharmacy.getPlace());
        holder.phone.setText(pharmacy.getPhoneNN());
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String phonee=pharmacy.getPhoneNN();
                intent.setData(Uri.parse("tel:" + phonee));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}