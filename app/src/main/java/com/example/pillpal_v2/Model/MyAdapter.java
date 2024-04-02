package com.example.pillpal_v2.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pillpal_v2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<User> list;
    private FirebaseFirestore db;

    public MyAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        User user = list.get(position);

        holder.tvMedicineName.setText(user.getMedicine_name());
        holder.tvprice.setText(user.getPrice());
        holder.tvQuantity.setText(user.getQuantity());
        holder.desc.setText(user.getDescription());
        Glide.with(context).load(user.getImageUrl()).into(holder.medimage);
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    deleteMed(adapterPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void deleteMed(int position) {
        User user = list.get(position);
        String documentId = user.getDocumentId();

        if (documentId != null) {
            db.collection("MT_ADDEdMedicine").document(documentId)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            list.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, list.size());
                            Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMedicineName, tvprice, tvQuantity,desc;
        ImageView ivDelete, medimage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMedicineName = itemView.findViewById(R.id.show_medicine_name);
            tvprice = itemView.findViewById(R.id.show_medicine_price);
            tvQuantity = itemView.findViewById(R.id.show_medicine_quantity);
            desc=itemView.findViewById(R.id.show_medicine_description);
            ivDelete = itemView.findViewById(R.id.show_medicine_delete);
            medimage = itemView.findViewById(R.id.show_medicine_image);
        }
    }
}
