package com.example.pillpal_v2.PatientClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pillpal_v2.Model.Medicine;
import com.example.pillpal_v2.Model.MedicineAdapter;
import com.example.pillpal_v2.Model.User;
import com.example.pillpal_v2.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ShowMedPatient extends AppCompatActivity {
    RecyclerView recyclerView;
    MedicineAdapter myAdapter;
    ArrayList<Medicine> list;
    FirebaseFirestore db;
    FirebaseAuth fAuth;
    private FirebaseStorage storage;
    private FirebaseFirestore fStore;
    CollectionReference df;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_med_patient);
        storage = FirebaseStorage.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        Button showsuppatient=findViewById(R.id.showmedpatient);
        showsuppatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowMedPatient.this, PatientHomePage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
        recyclerView = findViewById(R.id.medicine_list_recycler_Patient);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        list = new ArrayList<Medicine>();
        myAdapter = new MedicineAdapter(ShowMedPatient.this, list,"medicine");
        recyclerView.setAdapter(myAdapter);

        // Initialize the df variable
        df = fStore.collection("MT_ADDEdMedicine");

        df.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for (QueryDocumentSnapshot document : querySnapshot) {
                    String medicineName = document.getString("medicineName");
                    String documentID= document.getId();
                    String lattitude=document.getString("lattitude");
                    String longitude=document.getString("longitude");
                    String price = document.getString("price");
                    String place = document.getString("place");
                    String description=document.getString("description");
                    String pharmaName = document.getString("PharmaName");
                    String path="AdminImages/"+medicineName;
                    StorageReference imageRef = storage.getReference().child(path);

                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        Medicine medicine = new Medicine(medicineName, price, pharmaName, place, imageUrl,path,documentID,description,lattitude,longitude);
                        list.add(medicine);
                        myAdapter.notifyDataSetChanged();

                    });
                }

            }
        });
    }
}
