package com.example.pillpal_v2.PatientClasses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pillpal_v2.Model.FindMedAdapter;
import com.example.pillpal_v2.Model.FindMedConstructor;
import com.example.pillpal_v2.Model.MyAdapter;
import com.example.pillpal_v2.Model.User;
import com.example.pillpal_v2.PharmacyClasses.ShowMedPharma;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class FindMedActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    private RecyclerView recyclerView;
    private FindMedAdapter pharmacyAdapter;
    private List<FindMedConstructor> pharmacyList;
    String medicineName,medName;
    ArrayList<FindMedConstructor>  list;
    String phone;
    DocumentReference df;
    SharedPreferences sharedPreferences;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_med);
        //sharedPreferences = getSharedPreferences("MyPharmaData", Context.MODE_PRIVATE);
        medicineName = getIntent().getStringExtra("DOCUMENT_ID");
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        // Assuming you have initialized the pharmacyList with data
        recyclerView = findViewById(R.id.find_med_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<FindMedConstructor>();
        pharmacyAdapter = new FindMedAdapter(FindMedActivity.this,list);
        recyclerView.setAdapter(pharmacyAdapter);
        EventChangeListener(medicineName);
    }
    private void EventChangeListener(String medicineName) {
        fStore.collection("MT_ADDEdMedicine").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("FIrestore error ", error.getMessage());
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        DocumentSnapshot document = dc.getDocument();
                        if (document.contains("medicineName")) {
                            medName = document.getString("medicineName");

                            if (medName != null && medName.equals(medicineName)) {

                                list.add(document.toObject(FindMedConstructor.class));
                                 // Call notifyDataSetChanged() on the instance of FindMedAdapter
                            }


                        }

                    }
                }
                pharmacyAdapter.notifyDataSetChanged();
            }
        });
    }




}