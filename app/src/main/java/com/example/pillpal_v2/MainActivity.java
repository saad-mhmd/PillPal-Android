package com.example.pillpal_v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pillpal_v2.PatientClasses.PatientHomePage;
import com.example.pillpal_v2.PharmacyClasses.DesignHomePharmacist;
import com.example.pillpal_v2.SignInSignup.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore fStore;
    private String isPharma, isPatient;
    private DocumentReference dd, df;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance();

        fStore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Tawil Belak :) ");

    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.show();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        if (user != null) {
            String loggedInEmail = user.getEmail();
            Toast.makeText(MainActivity.this, loggedInEmail, Toast.LENGTH_LONG).show();
            df = fStore.collection("users").document(loggedInEmail);

            df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            isPharma = documentSnapshot.getString("isPharma");
                            isPatient = documentSnapshot.getString("isPatient");

                            if ("1".equals(isPharma)) {

                                startActivity(new Intent(MainActivity.this, DesignHomePharmacist.class));
                                finish();
                            } else {

                                startActivity(new Intent(MainActivity.this, PatientHomePage.class));
                                finish();
                            }
                            progressDialog.dismiss();
                        }
                    }
                }
            });
        } else {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();

        }


    }
}
