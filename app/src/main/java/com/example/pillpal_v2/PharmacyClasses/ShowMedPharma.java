package com.example.pillpal_v2.PharmacyClasses;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pillpal_v2.Model.MyAdapter;
import com.example.pillpal_v2.Model.Product;
import com.example.pillpal_v2.Model.User;
import com.example.pillpal_v2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ShowMedPharma extends AppCompatActivity {
    RecyclerView recyclerView;
    String inventoryShare, Inventory__ID;
    SharedPreferences sharedPreferences;
    MyAdapter myAdapter;
    String imageUrl;
    ArrayList<User> list;
    FirebaseFirestore db;
    String description;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    DocumentReference df,dm;
    String emailf;
    String inventoryId;
    String loggedInEmail, loggedInInventory, loggedInPhone, loggedInPharmaName, loggedInLocation;

    private FirebaseStorage storage;

    private FirebaseFirestore fStore;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_med_pharma);
        storage = FirebaseStorage.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        if (user != null) {
            emailf = user.getEmail(); // Retrieve the email address}
        }

        df = fStore.collection("PharmaUsers").document(emailf);

        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Inventory__ID = documentSnapshot.getString("Inventory_ID");
                        EventChangeListener(Inventory__ID);
                    }
                }
            }
        });

        sharedPreferences = getSharedPreferences("MyPharmaData", MODE_PRIVATE);
        getPharmacyShareData();
        inventoryShare = sharedPreferences.getString("randomKey", "");

        recyclerView = findViewById(R.id.medicine_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        list = new ArrayList<User>();
        myAdapter = new MyAdapter(ShowMedPharma.this, list);
        recyclerView.setAdapter(myAdapter);
    }

    private void EventChangeListener(String inva) {
        db.collection("MT_ADDEdMedicine").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("FIrestore error ", error.getMessage());
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        DocumentSnapshot document = dc.getDocument();
                        if (document.contains("InventoryID")) {
                            inventoryId = document.getString("InventoryID");
                            if (inventoryId != null && inventoryId.equals(inva)) {
                                String name = document.getString("medicineName");
                                String price = document.getString("price");
                                String quantity = document.getString("quantity");
                                String desc=document.getString("description");

                                String documentId = document.getId(); // Get the document ID
                                String path1 = "AdminImages/" + name;
                                StorageReference imageRef = storage.getReference().child(path1);
                                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();
                                    User user = new User(name, price, quantity, loggedInInventory, documentId, imageUrl,desc);
                                    list.add(user);
                                    myAdapter.notifyDataSetChanged();

                                });

                            }

                        }
                    }
                }
            }
        });
    }

    private void getPharmacyShareData() {
        loggedInEmail = sharedPreferences.getString("loggedInEmail", "");
        loggedInInventory = sharedPreferences.getString("loggedInInventory", "");
        loggedInPhone = sharedPreferences.getString("loggedInPhone", "");
        loggedInPharmaName = sharedPreferences.getString("loggedInPharmaName", "");
        loggedInLocation = sharedPreferences.getString("loggedInLocation", "");

    }

    // Implement the deleteMed method to handle item deletion
    public void deleteMed(int position) {
        myAdapter.deleteMed(position);
    }

    public void onButtonClick(View view) {
        startActivity(new Intent(getApplicationContext(), DesignHomePharmacist.class));
        finish();
    }
}