package com.example.pillpal_v2.PharmacyClasses;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pillpal_v2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddToMedList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_med_list);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        EditText editText1 = findViewById(R.id.editText1);
        EditText editText2 = findViewById(R.id.editText2);
        EditText editText3 = findViewById(R.id.editText3);
        EditText editText4 = findViewById(R.id.editText4);
        EditText editText5 = findViewById(R.id.editText5);
        EditText editText6 = findViewById(R.id.editText6);
        EditText editText7 = findViewById(R.id.editText7);
        EditText editText8 = findViewById(R.id.editText8);
        EditText editText9 = findViewById(R.id.editText9);
        EditText editText10 = findViewById(R.id.editText10);
        EditText editText11 = findViewById(R.id.editText11);
        EditText editText12 = findViewById(R.id.editText12);
        EditText editText13 = findViewById(R.id.editText13);
        EditText editText14 = findViewById(R.id.editText14);
        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a map to hold the data from the input views
                Map<String, Object> data = new HashMap<>();
                //"k" is the title of the field that appears on FireStore
                data.put("input1", editText1.getText().toString());
                data.put("input2", editText2.getText().toString());
                data.put("input3", editText3.getText().toString());
                data.put("input4", editText4.getText().toString());
                data.put("input5", editText5.getText().toString());
                data.put("input6", editText6.getText().toString());
                data.put("input7", editText7.getText().toString());
                data.put("input8", editText8.getText().toString());
                data.put("input9", editText9.getText().toString());
                data.put("input10", editText10.getText().toString());
                data.put("input11", editText11.getText().toString());
                data.put("input12", editText12.getText().toString());
                data.put("input13", editText13.getText().toString());
                data.put("input14", editText14.getText().toString());

                // Show an alert dialog to get the name of the document
                AlertDialog.Builder builder = new AlertDialog.Builder(AddToMedList.this);
                builder.setTitle("Enter document name");

                final EditText input = new EditText(AddToMedList.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String documentName = input.getText().toString().toLowerCase();
                        if (documentName.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Please enter a name for the document", Toast.LENGTH_SHORT).show();
                        } else {
                            // Add the data to Firestore with the specified document name
                            db.collection("MS_NewMedList").document(documentName).set(data)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getApplicationContext(), "Data added successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Error adding data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });


    }
}