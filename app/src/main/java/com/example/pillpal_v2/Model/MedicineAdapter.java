package com.example.pillpal_v2.Model;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pillpal_v2.PatientClasses.PatientHomePage;
import com.example.pillpal_v2.PatientClasses.ProductsInfo;
import com.example.pillpal_v2.PatientClasses.ShowMedPatient;
import com.example.pillpal_v2.R;

import java.util.ArrayList;
import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {
    private Context context;

    private List<Medicine> list;
    private String index; // Added index variable to hold the class type

    public MedicineAdapter(Context context, ArrayList<Medicine> list,String index) {
        this.context = context;
        this.list = list;
        this.index=index;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.showmeditempatient, parent, false);
        return new MedicineViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = list.get(position);

        holder.medicineName.setText(medicine.getName());


        holder.medicinePrice.setText(medicine.getPrice());
        holder.medicinePharma.setText(medicine.getPharmacy());
        holder.medicinePharmaPlace.setText(medicine.getPharmacyPlace());
        holder.medionclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index.equals("experts")){}
                else {
                    Intent intent = new Intent(context, ProductsInfo.class);
                    String medname = medicine.getName();

                    intent.putExtra("index", index);
                    intent.putExtra("description", medicine.getDescription());
                    intent.putExtra("lattitude", medicine.getLattitude());
                    intent.putExtra("longitude", medicine.getLongitude());
                    intent.putExtra("medicineName", medname);
                    intent.putExtra("pharmacyName", medicine.getPharmacy());
                    intent.putExtra("pharmaplace", medicine.getPharmacyPlace());
                    intent.putExtra("productPrice", medicine.getPrice());
                    intent.putExtra("productspath", medicine.getPath());
                    intent.putExtra("documentID", medicine.getDocumentID());
                    context.startActivity(intent);


                }
            }
        });
        Glide.with(context).load(medicine.getImageUrl()).into(holder.medicineImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MedicineViewHolder extends RecyclerView.ViewHolder {
        private ImageView medicineImage;
        private TextView medicineName;
        private TextView medicineDescription;
        private TextView medicineCurrency;
        private TextView medicinePrice;
        private TextView medicinePharma;
        LinearLayout medionclick;
        private TextView medicinePharmaPlace;

        public MedicineViewHolder(View itemView) {
            super(itemView);
            medionclick=itemView.findViewById(R.id.medionclick);
            medicineImage = itemView.findViewById(R.id.show_medicine_image);
            medicineName = itemView.findViewById(R.id.mednamep);
            medicineCurrency = itemView.findViewById(R.id.show_medicine_currency);
            medicinePrice = itemView.findViewById(R.id.medpricep);
            medicinePharma = itemView.findViewById(R.id.medpharmap);
            medicinePharmaPlace = itemView.findViewById(R.id.medpharmplacep);

        }
    }
}
