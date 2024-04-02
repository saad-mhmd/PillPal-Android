package com.example.pillpal_v2.Model;

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

import java.util.List;

public class SupplementAdapter extends RecyclerView.Adapter<SupplementAdapter.SupplementViewHolder> {

    private Context context;
    private List<Supplement> supplementList;
    private FirebaseFirestore db;

    public SupplementAdapter(Context context, List<Supplement> supplementList) {
        this.context = context;
        this.supplementList = supplementList;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public SupplementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_suplement, parent, false);
        return new SupplementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplementViewHolder holder, int position) {
        Supplement supplement = supplementList.get(position);
        holder.supplementName.setText(supplement.getName());
        holder.supplementPrice.setText(supplement.getPrice());
        holder.supplementQuantity.setText(supplement.getQuantity());
        holder.supplementDescription.setText(supplement.getDescription());
        Glide.with(context).load(supplement.getImageUrl()).into(holder.supplementImage);
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
        return supplementList.size();
    }
    public void deleteMed(int position) {
        Supplement user = supplementList.get(position);
        String documentId = user.getDocumentId();

        if (documentId != null) {
            db.collection("MT_ADDedSuplement").document(documentId)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            supplementList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, supplementList.size());
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
    public static class SupplementViewHolder extends RecyclerView.ViewHolder {
        ImageView supplementImage,ivDelete;
        TextView supplementName;
        TextView supplementPrice;
        TextView supplementQuantity;
        TextView supplementDescription;


        public SupplementViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDelete=itemView.findViewById(R.id.suplement_delete);
            supplementImage = itemView.findViewById(R.id.suplement_image);
            supplementName = itemView.findViewById(R.id.suplement_name);
            supplementPrice = itemView.findViewById(R.id.suplement_price);
            supplementQuantity = itemView.findViewById(R.id.suplement_quantity);
            supplementDescription = itemView.findViewById(R.id.suplement_description);
        }
    }
}
