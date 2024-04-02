package com.example.pillpal_v2.PatientClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.pillpal_v2.PharmacyClasses.DesignHomePharmacist;
import com.example.pillpal_v2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class PatientInfo extends AppCompatActivity {
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText addressEditText;
    private EditText phoneNumberEditText;
    private EditText dobEditText;
    private Button submitButton;
    private FirebaseFirestore db, dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        // Get Firestore instance
        db = FirebaseFirestore.getInstance();
        dm = FirebaseFirestore.getInstance();
        // Get views by ID
        firstNameEditText = findViewById(R.id.editTextPatientFirstName);
        lastNameEditText = findViewById(R.id.editTextLastName);
        addressEditText = findViewById(R.id.editTextAddress);
        phoneNumberEditText = findViewById(R.id.pnumber);
        dobEditText = findViewById(R.id.editTextDOB);
        submitButton = findViewById(R.id.patientSubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            String patientF, lname, padress, email, pbirthday, pNumber;

            @Override
            public void onClick(View view) {
                pbirthday = dobEditText.getText().toString().trim();

                pNumber = phoneNumberEditText.getText().toString().trim();
                patientF = firstNameEditText.getText().toString().trim();
                lname = lastNameEditText.getText().toString().trim();
                padress = addressEditText.getText().toString().trim();
                email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                // Create a new document with email as the document ID
                Map<String, Object> PatientsInfo = new HashMap<>();
                PatientsInfo.put("First Name", patientF);
                PatientsInfo.put("Last Name", lname);
                PatientsInfo.put("Date of birth", pbirthday);
                PatientsInfo.put("Phone Number", pNumber);
                PatientsInfo.put("address", padress);
                // Add data to Firestore
                db.collection("PatientsInfo").document(email)
                        .set(PatientsInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Patient Info added successfully.", Toast.LENGTH_SHORT).show();
                                firsttimeloginupdate(email);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Error adding Pharmacy Info. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void firsttimeloginupdate(String email) {


        String collectionName = "users";
        String documentId = email;

        // Create the update data
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("first time login ", "0");

        // Create a reference to the document
        DocumentReference documentRef = dm.collection(collectionName).document(documentId);

        // Perform the update with set() method and SetOptions.merge()
        documentRef.set(updateData, SetOptions.merge());

        // Open Pharmacy activity
        startActivity(new Intent(getApplicationContext(), PatientHomePage.class));
        finish();
    }
}