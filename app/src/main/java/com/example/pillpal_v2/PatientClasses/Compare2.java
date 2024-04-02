package com.example.pillpal_v2.PatientClasses;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pillpal_v2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Compare2 extends AppCompatActivity {
    DocumentReference db;
    FirebaseFirestore fStore;
    Spinner s1, s2;

    Button comparebtn, refresh;
    TextView tvtest1, compare_med1_name;
    String spec1, spec2,name11,name22;
    TextView comMedName2, compareMedName2, tv1, tv2;
    String medName1, documentName1, documentName2, medName2, documentName, ATCvalue1, ATCvalue2, Agent1, Agent2, Brand_Generic1, Brand_Generic2, Code1, Code2;
    String Country1, Country2, Form1, Form2, Ingredients1, Ingredients2, Manufacturer1, Manufacturer2, Presentation1, Presentation2;
    String Registration_Number1, Registration_Number2, Route1, Route2, Stratum1, Stratum2, Subsidy_Percentage1, Subsidy_Percentage2;
    TextView Agt1, Agt2, BrandGeneric1, BrandGeneric2, Cd1, Cd2;
    TextView Ctry1, Ctry2, Fm1, Fm2, Ingrs1, Ingrs2, Manuf1, Manuf2, Present1, Present2;
    TextView RegistrationNumber1, RegistrationNumber2, Rt1, Rt2, Str1, Str2, Sub_Percentage1, Sub_Percentage2;
    TextView compare_med1_specifications1, compare_med2_specifications2;
    TextView compare_med2_name;
    ImageView medImage1, medImage2;
    TextView name111,name222;
    FirebaseStorage storage;
    StorageReference storageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare2);
         storage = FirebaseStorage.getInstance();

        medImage2 = findViewById(R.id.compare_med2_image);
        medImage1 = findViewById(R.id.compare_med1_image);
        compare_med1_name = findViewById(R.id.compare_med1_name);
         compare_med2_name=findViewById(R.id.compare_med2_name);
        fStore = FirebaseFirestore.getInstance();
        compare_med1_specifications1 = findViewById(R.id.compare_med1_specifications);
        compare_med2_specifications2 = findViewById(R.id.compare_med2_specifications);
        refresh = findViewById(R.id.compare_refresh);
        s1 = findViewById(R.id.compare_spinner1);
        s2 = findViewById(R.id.compare_spinner2);
        Agt1 = findViewById(R.id.compare_med1_detail2_value);
        Agt2 = findViewById(R.id.compare_med2_detail2_value);
        BrandGeneric1 = findViewById(R.id.compare_med1_detail3_value);
        BrandGeneric2 = findViewById(R.id.compare_med2_detail3_value);
        comparebtn = findViewById(R.id.compare_compare_button);





        Cd1 = findViewById(R.id.compare_med1_detail4_value);
        Cd2 = findViewById(R.id.compare_med2_detail4_value);
        tv1 = findViewById(R.id.compare_med1_detail1_value);
        tv2 = findViewById(R.id.compare_med2_detail1_value);
        Ctry1 = findViewById(R.id.compare_med1_detail5_value);
        Ctry2 = findViewById(R.id.compare_med2_detail5_value);
        Fm1 = findViewById(R.id.compare_med1_detail6_value);
        Fm2 = findViewById(R.id.compare_med2_detail6_value);
        Ingrs1 = findViewById(R.id.compare_med1_detail7_value);
        Ingrs2 = findViewById(R.id.compare_med2_detail7_value);
        Manuf1 = findViewById(R.id.compare_med1_detail8_value);
        Manuf2 = findViewById(R.id.compare_med2_detail8_value);
        name111=findViewById(R.id.compare_med1_detail9_value);
        name222=findViewById(R.id.compare_med2_detail9_value);
        Present1 = findViewById(R.id.compare_med1_detail10_value);
        Present2 = findViewById(R.id.compare_med2_detail10_value);
        RegistrationNumber1 = findViewById(R.id.compare_med1_detail11_value);
        RegistrationNumber2 = findViewById(R.id.compare_med2_detail11_value);
        Rt1 = findViewById(R.id.compare_med1_detail12_value);
        Rt2 = findViewById(R.id.compare_med2_detail12_value);
        Str1 = findViewById(R.id.compare_med1_detail13_value);
        Str2 = findViewById(R.id.compare_med2_detail13_value);
        Sub_Percentage1 = findViewById(R.id.compare_med1_detail14_value);
        Sub_Percentage2 = findViewById(R.id.compare_med2_detail14_value);


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSpinnerValues();
            }
        });


        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                medName1 = parent.getItemAtPosition(position).toString();
                documentName1 = medName1;

                compare_med1_name.setText(medName1);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                medName2 = parent.getItemAtPosition(position).toString();
                documentName2 = medName2;
                compare_med2_name.setText(medName2);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }
    private void EventChangeListener1(String medName1) {
        db = fStore.collection("MT_MEDICINELIST").document(documentName1);
        db.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        ATCvalue1 = documentSnapshot.getString("ATC");
                        Agent1 = documentSnapshot.getString("Agent");
                        Brand_Generic1 = documentSnapshot.getString("Brand_Generic");
                        Code1 = documentSnapshot.getString("Code");
                        Country1 = documentSnapshot.getString("Country");
                        Form1 = documentSnapshot.getString("Form");
                         name11=documentSnapshot.getString("Name");
                        Ingredients1 = documentSnapshot.getString("Igredients");
                        Manufacturer1 = documentSnapshot.getString("Manufacturer");
                        Presentation1 = documentSnapshot.getString("Presentation");
                        //Price_LBP1 = documentSnapshot.getString("Price");
                        Registration_Number1 = documentSnapshot.getString("RegistrationNumber");
                        Route1 = documentSnapshot.getString("Route");
                        Stratum1 = documentSnapshot.getString("Stratum");
                        Subsidy_Percentage1 = documentSnapshot.getString("SubsidyPercentage");
                        spec1 = documentSnapshot.getString("description");
                        tv1.setText(ATCvalue1);
                        Agt1.setText(Agent1);
                        BrandGeneric1.setText(Brand_Generic1);
                        Cd1.setText(Code1);
                        Ctry1.setText(Country1);
                        Fm1.setText(Form1);
                        Ingrs1.setText(Ingredients1);
                        Manuf1.setText(Manufacturer1);
                        name111.setText(name11);
                        Present1.setText(Presentation1);
                        RegistrationNumber1.setText(Registration_Number1);

                        Rt1.setText(Route1);
                        Str1.setText(Stratum1);
                        Sub_Percentage1.setText(Subsidy_Percentage1);
                        compare_med1_specifications1.setText(spec1);


                    }
                }
            }
        });
    }
    private void EventChangeListener2(String medName2) {
        db = fStore.collection("MT_MEDICINELIST").document(medName2);
        db.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        ATCvalue2 = documentSnapshot.getString("ATC");
                        Agent2 = documentSnapshot.getString("Agent");
                        Brand_Generic2 = documentSnapshot.getString("Brand_Generic");
                        Code2 = documentSnapshot.getString("Code");
                        Country2 = documentSnapshot.getString("Country");
                        Form2 = documentSnapshot.getString("Form");
                        Ingredients2 = documentSnapshot.getString("Igredients");
                         name22=documentSnapshot.getString("Name");
                        Manufacturer2 = documentSnapshot.getString("Manufacturer");
                        Presentation2 = documentSnapshot.getString("Presentation");
                        Registration_Number2 = documentSnapshot.getString("RegistrationNumber");
                        Route2 = documentSnapshot.getString("Route");
                        Stratum2 = documentSnapshot.getString("Stratum");
                        Subsidy_Percentage2 = documentSnapshot.getString("SubsidyPercentage");
                        spec2 = documentSnapshot.getString("description");
                        tv2.setText(ATCvalue2);
                        Agt2.setText(Agent2);
                        BrandGeneric2.setText(Brand_Generic2);
                        Cd2.setText(Code2);
                        Ctry2.setText(Country2);
                        Fm2.setText(Form2);
                        Ingrs2.setText(Ingredients2);
                        Manuf2.setText(Manufacturer2);
                        Present2.setText(Presentation2);
                        name222.setText(name22);
                        RegistrationNumber2.setText(Registration_Number2);
                        Rt2.setText(Route2);
                        Str2.setText(Stratum2);
                        Sub_Percentage2.setText(Subsidy_Percentage2);

                        compare_med2_specifications2.setText(spec2);
                    }
                }
            }
        });
    }
    private void setSpinnerValues() {
        List<String> documentNames = new ArrayList<>();
        // Get a reference to the "test" collection
        CollectionReference collectionRef = FirebaseFirestore.getInstance().collection("MT_MEDICINELIST");
        // Retrieve all documents in the "test" collection
        collectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG, "Number of documents retrieved: " + queryDocumentSnapshots.size());
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    // Get the document ID (name) from each document and add it to the documentNames ArrayList
                    documentName = documentSnapshot.getId();
                    documentNames.add(documentName);
                }
                Collections.sort(documentNames, String.CASE_INSENSITIVE_ORDER);
                // Set the spinner adapter with the documentNames ArrayList
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Compare2.this, android.R.layout.simple_spinner_dropdown_item, documentNames);
                s1.setAdapter(adapter);
                s2.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error getting documents: " + e.getMessage());
            }
        });
    }
    public void comparebtnps(View view) {
        loadImage1(medName1);
        loadImage2(medName2);
        EventChangeListener2(medName2);
        EventChangeListener1(medName1);

    }

    private void loadImage2(String medName2) {
         storageRef = storage.getReference().child("AdminImages/").child(medName2);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(medImage2);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    private void loadImage1(String medName1) {
         storageRef = storage.getReference().child("AdminImages").child(medName1);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(medImage1);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}