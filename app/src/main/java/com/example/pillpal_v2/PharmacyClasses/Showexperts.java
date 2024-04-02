package com.example.pillpal_v2.PharmacyClasses;

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
import com.example.pillpal_v2.PatientClasses.PatientHomePage;
import com.example.pillpal_v2.PatientClasses.ShowProdcutPatient;
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

public class Showexperts extends AppCompatActivity {
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
        setContentView(R.layout.activity_showexperts);
        storage = FirebaseStorage.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
         Button showsuppatient=findViewById(R.id.showexppatient);
        showsuppatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Showexperts.this, PatientHomePage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        recyclerView = findViewById(R.id.experts_list_recycler_Patient);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        list = new ArrayList<Medicine>();
        myAdapter = new MedicineAdapter(Showexperts.this, list,"experts");
        recyclerView.setAdapter(myAdapter);

        // Initialize the df variable
        df = fStore.collection("experts");

        df.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for (QueryDocumentSnapshot document : querySnapshot) {
                    String medicineName = document.getString("Name");
                    String phonem = document.getString("PhoneNumber");
                    String place = document.getString("Place");
                    String email = document.getString("Email");
                    String documentID= document.getId();
                  String path="ExpertImages/"+documentID;

                    String description=document.getString("description");
                    StorageReference imageRef = storage.getReference().child(path);
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        Medicine medicine = new Medicine(medicineName, phonem, email, place, imageUrl,path,documentID,description,"","");
                        list.add(medicine);
                        myAdapter.notifyDataSetChanged();
                        // Process the fields as needed
                        // For example, add the document data to your ArrayList or RecyclerView adapter

                    });

                }
                // Update your RecyclerView adapter here with the updated ArrayList

            }
        });
    }
}