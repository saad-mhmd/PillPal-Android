package com.example.pillpal_v2.PharmacyClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pillpal_v2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DesignHomePharmacist extends AppCompatActivity {
    LinearLayout showInventoryPanel, addMedicinePanel, addProductPanel, showProductPanel, showSuplementpanel, addsuplementpanel;
    SharedPreferences sharedPreferences;
    String loggedInEmail, loggedInInventory, loggedInPhone, loggedInPharmaName, loggedInLocation,loggedInLong,loggedInlatt;
    TextView pharmacistNameText;

    Button logout;
    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    FirebaseUser user;
    DocumentReference df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_home_pharmacist);

        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        logout=findViewById(R.id.logoutpharma);

        sharedPreferences = getSharedPreferences("MyPharmaData", MODE_PRIVATE);
        setSharepref();
        pharmacistNameText = findViewById(R.id.pharmacistNameText);

        showSuplementpanel = findViewById(R.id.showSupplementPanel);
        addsuplementpanel = findViewById(R.id.addSupplementPanel);
        addsuplementpanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DesignHomePharmacist.this, AddSuplement.class);
                startActivity(intent);
            }
        });
        showSuplementpanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DesignHomePharmacist.this, ShowSuplement.class);
                startActivity(intent);

            }
        });
        showProductPanel = findViewById(R.id.showProductPanel);
        showProductPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DesignHomePharmacist.this, ShowPharmaProducts.class);
                startActivity(intent);
            }
        });
        addProductPanel = findViewById(R.id.addProductPanel);
        addProductPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DesignHomePharmacist.this, AddProducts.class);
                startActivity(intent);
            }
        });
        showInventoryPanel = findViewById(R.id.showMedicinePanel);
        addMedicinePanel = findViewById(R.id.addMedicinePanel);
        showInventoryPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DesignHomePharmacist.this, ShowMedPharma.class);
                startActivity(intent);

            }
        });
        addMedicinePanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DesignHomePharmacist.this, AddMedecine.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut();
                startActivity(new Intent(DesignHomePharmacist.this, Login.class));
                finish();
                Toast.makeText(DesignHomePharmacist.this, "Logout clicked", Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void setSharepref() {

        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("MyPharmaData", MODE_PRIVATE);


        if (user != null) {
            loggedInEmail = user.getEmail(); // Retrieve the email address
        }

        df = db.collection("PharmaUsers").document(loggedInEmail);

        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        loggedInInventory = documentSnapshot.getString("Inventory_ID");
                        loggedInPhone = documentSnapshot.getString("PhoneNumber");

                        saveDataToSharedPreferences(); // Save the values in shared preferences
                    }
                }
            }

        });

        df = db.collection("pharmacyInfo").document(loggedInEmail);

        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        loggedInPharmaName = documentSnapshot.getString("pharmaName");
                        loggedInLocation = documentSnapshot.getString("place");
                        loggedInLong=documentSnapshot.getString("longitude");
                        loggedInlatt=documentSnapshot.getString("lattitude");
                        saveDataToSharedPreferences(); // Save the values in shared preferences
                    }
                }
            }

        });


    }

    private void saveDataToSharedPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("loggedInEmail", loggedInEmail);
        editor.putString("longitude",loggedInLong);

        editor.putString("lattitude",loggedInlatt);
        editor.putString("loggedInInventory", loggedInInventory);
        editor.putString("loggedInPhone", loggedInPhone);
        editor.putString("loggedInPharmaName", loggedInPharmaName);
        editor.putString("loggedInLocation", loggedInLocation);
        editor.apply();
        pharmacistNameText.setText(loggedInPharmaName);
    }
}