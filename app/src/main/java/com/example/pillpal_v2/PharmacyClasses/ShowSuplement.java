package com.example.pillpal_v2.PharmacyClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.pillpal_v2.Model.Supplement;
import com.example.pillpal_v2.Model.SupplementAdapter;
import com.example.pillpal_v2.PatientClasses.PatientHomePage;
import com.example.pillpal_v2.PatientClasses.ShowProdcutPatient;
import com.example.pillpal_v2.R;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ShowSuplement extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String loggedInEmail, loggedInInventory, loggedInPhone, loggedInPharmaName, loggedInLocation;

    private static final String TAG = "ShowPharmaProducts";
    private RecyclerView recyclerView;
    private SupplementAdapter supplementAdapter;
    private List<com.example.pillpal_v2.Model.Supplement> supplementList;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    Button backProd;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_suplement);
        sharedPreferences = getSharedPreferences("MyPharmaData", Context.MODE_PRIVATE);
        getPharmacyShareData();
        backProd = findViewById(R.id.backSup);
        backProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowSuplement.this, DesignHomePharmacist.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
        recyclerView = findViewById(R.id.RecyclerViewSupplement);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        supplementList = new ArrayList<>();
        supplementAdapter = new SupplementAdapter(this, supplementList);
        recyclerView.setAdapter(supplementAdapter);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        loadData();
    }

    private void getPharmacyShareData() {
        loggedInEmail = sharedPreferences.getString("loggedInEmail", "");
        loggedInInventory = sharedPreferences.getString("loggedInInventory", "");
        loggedInPhone = sharedPreferences.getString("loggedInPhone", "");
        loggedInPharmaName = sharedPreferences.getString("loggedInPharmaName", "");
        loggedInLocation = sharedPreferences.getString("loggedInLocation", "");

    }

    private void loadData() {
        firestore.collection("MT_ADDedSuplement").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String Inv = document.getString("InventoryID");

                    if (loggedInInventory.equals(Inv)) {
                        String name = document.getString("Name");
                        String price = document.getString("price");
                        String quantity = document.getString("quantity");
                        String description = document.getString("description");

                        String documentId = document.getId(); // Get the document ID
                        String path1="SuplementImages/"+loggedInEmail+"/"+name;
                        StorageReference imageRef = storage.getReference().child(path1);
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            String DocumentId=document.getId();
                            Supplement supplement = new Supplement(name, price, quantity, description, imageUrl,DocumentId);
                            supplementList.add(supplement);
                            supplementAdapter.notifyDataSetChanged();
                        });
                        imageRef.getDownloadUrl().addOnFailureListener(e -> {
                            String errorMessage = e.getMessage();
                            Log.e(TAG, "Error getting image URL: " + errorMessage, e);
                        });
                    }
                }
            } else {
                Log.e(TAG, "Error getting documents", task.getException());
            }
        });
    }

}
