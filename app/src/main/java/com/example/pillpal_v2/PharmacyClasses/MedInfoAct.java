package com.example.pillpal_v2.PharmacyClasses;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pillpal_v2.PatientClasses.Compare2;
import com.example.pillpal_v2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MedInfoAct extends AppCompatActivity {
    RecyclerView recyclerView;


    String input1, input2, input3, input5, input4, input6, input7, input8, input9, input10, input11, input12, input13, input14,input15;
    TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11, tv12, tv13, tv14,tv15;
    private FirebaseFirestore fStore;
    DocumentReference db;
    String documentId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_info);
        tv1 = findViewById(R.id.tvinput1);
        tv2 = findViewById(R.id.tvinput2);
        tv3 = findViewById(R.id.tvinput3);
        tv4 = findViewById(R.id.tvinput4);
        tv5 = findViewById(R.id.tvinput5);
        tv6 = findViewById(R.id.tvinput6);
        tv7 = findViewById(R.id.tvinput7);
        tv8 = findViewById(R.id.tvinput8);
        tv9 = findViewById(R.id.tvinput9);
        tv10 = findViewById(R.id.tvinput10);
        tv11 = findViewById(R.id.tvinput11);
        tv12 = findViewById(R.id.tvinput12);
        tv13 = findViewById(R.id.tvinput13);
        tv14 = findViewById(R.id.tvinput14);
        tv15=findViewById(R.id.tvinput15);


        Intent intent = getIntent();
        documentId = intent.getStringExtra("DOCUMENT_ID");
        fStore = fStore.getInstance();



        EventChangeListener(documentId);


    }

    private void EventChangeListener(String documentId) {

        db = fStore.collection("MT_MEDICINELIST").document(documentId);

        db.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        input1 = documentSnapshot.getString("ATC");
                        input2 = documentSnapshot.getString("Agent");
                        input3 = documentSnapshot.getString("Brand_Generic");
                        input4 = documentSnapshot.getString("Code");
                        input5 = documentSnapshot.getString("Country");
                        input6 = documentSnapshot.getString("Form");
                        input7 = documentSnapshot.getString("Igredients");
                        input8 = documentSnapshot.getString("Manufacturer");
                        input9 = documentSnapshot.getString("Name");
                        input10 = documentSnapshot.getString("Presentation");
                        input11 = documentSnapshot.getString("RegistrationNumber");
                        input12 = documentSnapshot.getString("Route");
                        input13 = documentSnapshot.getString("Stratum");
                        input14 = documentSnapshot.getString("SubsidyPercentage");
                        input15=documentSnapshot.getString("description");

                        tv1.setText(input1);
                        tv15.setText(input15);
                        tv2.setText(input2);
                        tv3.setText(input3);
                        tv4.setText(input4);
                        tv5.setText(input5);
                        tv6.setText(input6);
                        tv7.setText(input7);
                        tv8.setText(input8);
                        tv9.setText(input9);
                        tv10.setText(input10);
                        tv11.setText(input11);
                        tv12.setText(input12);
                        tv13.setText(input13);
                        tv14.setText(input14);

                    }
                }
            }
        });
    }


    public void onButtonClick(View view) {
    }

    public void goToCompare(View view) {
        startActivity(new Intent(getApplicationContext(), Compare2.class));
        finish();

    }
}
