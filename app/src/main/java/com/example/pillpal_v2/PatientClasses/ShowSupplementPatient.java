package com.example.pillpal_v2.PatientClasses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pillpal_v2.Model.Medicine;
import com.example.pillpal_v2.Model.MedicineAdapter;
import com.example.pillpal_v2.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ShowSupplementPatient extends AppCompatActivity {
    RecyclerView recyclerView;
    MedicineAdapter myAdapter;
    ArrayList<Medicine> list;
    FirebaseFirestore db;
    FirebaseAuth fAuth;
    private FirebaseStorage storage;
    private FirebaseFirestore fStore;
    CollectionReference df;
    FirebaseUser user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_supplement_patient);

        storage = FirebaseStorage.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        Button showsuppatient=findViewById(R.id.showsuppatient);
        showsuppatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowSupplementPatient.this, PatientHomePage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        recyclerView = findViewById(R.id.supplement_list_recycler_Patient);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        list = new ArrayList<Medicine>();
        myAdapter = new MedicineAdapter(ShowSupplementPatient.this, list,"suplement");
        recyclerView.setAdapter(myAdapter);

        // Initialize the df variable
        df = fStore.collection("MT_ADDedSuplement");

        df.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for (QueryDocumentSnapshot document : querySnapshot) {
                    String medicineName = document.getString("Name");
                    String price = document.getString("price");
                    String place = document.getString("place");
                    String pharmaName = document.getString("PharmaName");
                    String lattitude=document.getString("lattitude");
                    String longitude=document.getString("longitude");

                    String path=document.getString("path");
                    String description=document.getString("description");
                    String documentId= document.getId();
                    StorageReference imageRef = storage.getReference().child(path);
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        Medicine medicine = new Medicine(medicineName, price, pharmaName, place, imageUrl,path,documentId,description,lattitude,longitude);
                        list.add(medicine);
                        myAdapter.notifyDataSetChanged();
                        // Process the fields as needed
                        // For example, add the document data to your ArrayList or RecyclerView adapter

                    });

                    // Process the fields as needed
                    // For example, add the document data to your ArrayList or Recycl
                }

                // Update your RecyclerView adapter here with the updated ArrayList

            }
        });
    }
}