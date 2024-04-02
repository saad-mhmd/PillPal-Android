package com.example.pillpal_v2.PharmacyClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.pillpal_v2.R;
import com.example.pillpal_v2.TestLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;


public class PharmaInfo extends AppCompatActivity {
    private EditText name;
    private EditText email;
    private EditText pharmaphone;
    private EditText address;
    private EditText medicalInformation;
    private EditText pharmaRefernce;
    private FirebaseFirestore db, dm;


    private Button submitButton, getlocationb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharma_info);
        db = FirebaseFirestore.getInstance();
        dm = FirebaseFirestore.getInstance();
        name = findViewById(R.id.pharmaNameEditText);
        getlocationb = findViewById(R.id.getlocationb);
        email = findViewById(R.id.email_input);
        pharmaphone = findViewById(R.id.pharmaphoneNumber);
        address = findViewById(R.id.pharma_addresss);
        medicalInformation = findViewById(R.id.medical_info_input);
        pharmaRefernce = findViewById(R.id.pharmacy_preference_input);

        submitButton = findViewById(R.id.submitButton);

        // Set initial visibility of the submitButton to INVISIBLE


        // Set click listener for the submitButton

        submitButton.setOnClickListener(new View.OnClickListener() {
            String pharmaname, pharmaemail, pharmaphonee, pharmaaddress, pharmamedical, pharmareferncee;

            @Override
            public void onClick(View view) {
                pharmaname = name.getText().toString().trim();
                pharmaemail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                pharmaphonee = pharmaphone.getText().toString().trim();
                pharmaaddress = address.getText().toString().trim();
                pharmamedical = medicalInformation.getText().toString().trim();


                goToGetLocation(pharmaname, pharmaemail, pharmaphonee, pharmaaddress, pharmamedical);
                // Create a new document with email as the document ID
             /*   Map<String, Object> PatientsInfo = new HashMap<>();
                PatientsInfo.put("pharmaName", pharmaname);
                PatientsInfo.put("phone", pharmaphonee);
                PatientsInfo.put("place", pharmaaddress);
                PatientsInfo.put("medical info", pharmamedical);
                PatientsInfo.put("pharmacy preference", pharmareferncee);
                // Add data to Firestore
                db.collection("pharmacyInfo").document(pharmaemail)
                        .set(PatientsInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "pharmacy  Info added successfully.", Toast.LENGTH_SHORT).show();
                                firsttimelogin(pharmaemail);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Error adding Pharmacy Info. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });*/
            }
        });
    }

    private void goToGetLocation(String pharmaname, String pharmaemail, String pharmaphonee, String pharmaaddress, String pharmamedical) {
        Intent intent = new Intent(PharmaInfo.this, TestLocation.class);
        intent.putExtra("pharmaname", pharmaname);

        intent.putExtra("pharmaemail", pharmaemail);
        intent.putExtra("pharmaphone", pharmaphonee);
        intent.putExtra("pharmaaddress", pharmaaddress);
        intent.putExtra("pharmamedical", pharmamedical);
        startActivity(intent);

    }

    private void firsttimelogin(String pharmaemail) {


        String collectionName = "users";
        String documentId = pharmaemail;

        // Create the update data
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("first time login ", "0");

        // Create a reference to the document
        DocumentReference documentRef = dm.collection(collectionName).document(documentId);

        // Perform the update with set() method and SetOptions.merge()
        documentRef.set(updateData, SetOptions.merge());

        // Open Pharmacy activity

        startActivity(new Intent(getApplicationContext(), DesignHomePharmacist.class));
        finish();

    }


}
