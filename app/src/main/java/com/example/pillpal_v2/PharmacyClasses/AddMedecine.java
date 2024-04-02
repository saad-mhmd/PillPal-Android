package com.example.pillpal_v2.PharmacyClasses;

import static android.content.ContentValues.TAG;

import static com.example.pillpal_v2.R.layout.activity_add_medecine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pillpal_v2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AddMedecine extends AppCompatActivity {
    private Spinner brandSpinner;

    private static final String TAG = "AddMedicine";
    private TextInputEditText priceEditText, quantityEditText;
    private SharedPreferences sharedPreferences;
    private Button addButton, quantityminus, quantityplus;
    ImageView imageButtonMedicine;


    String selectedBrand;
    private FirebaseAuth fAuth;
    FirebaseFirestore db, mm, fStore;
    FirebaseUser user;
    String loggedInEmail, loggedInInventory, loggedInPhone, loggedInPharmaName, loggedInLocation,loggedInlatt,loggedInlong;

    FirebaseStorage storage;
    StorageReference storageRef;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_add_medecine);
        storage = FirebaseStorage.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        mm = FirebaseFirestore.getInstance();
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences("MyPharmaData", Context.MODE_PRIVATE);
        getPharmacyShareData();
        quantityEditText = findViewById(R.id.edit_text_quantity);
        quantityEditText.setText("1");
        quantityplus = findViewById(R.id.quantity_plus);
        quantityminus = findViewById(R.id.quantity_minus);

        quantityplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseQuantity();
            }
        });
        quantityminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseQuantity();
            }
        });

        brandSpinner = findViewById(R.id.spinnerBrand);
        setBrandSpinnerValues();
        imageButtonMedicine = findViewById(R.id.imageButtonMedicine);


        // Initialize views

        brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBrand = parent.getItemAtPosition(position).toString();
                loadimage(selectedBrand);
                // Do something with the selected brand
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
        priceEditText = findViewById(R.id.edit_text_price);

        addButton = findViewById(R.id.add_medicine_button);
        // Set spinner values
        setBrandSpinnerValues();


        // Set click listener for add button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if required fields are not empty

                if (selectedBrand == null) {
                    // The user did not select a brand
                    // Show an error message
                    // ...
                    return;

                } else if (priceEditText.getText().toString().trim().isEmpty()) {
                    priceEditText.setError("Please enter price.");
                    return;
                } else if (quantityEditText.getText().toString().trim().isEmpty()) {
                    quantityEditText.setError("Please enter quantity.");
                    return;
                }
                String path="AdminImages/"+selectedBrand.toLowerCase();


                String randomKey = generateRandomStringKey(8);

                // Create a new medicine map for the new row
                Map<String, Object> newMedicine = new HashMap<>();
                newMedicine.put("medicineName", selectedBrand.toLowerCase());

                newMedicine.put("price", priceEditText.getText().toString().trim());
                newMedicine.put("quantity", quantityEditText.getText().toString().trim());
                newMedicine.put("PharmaName", loggedInPharmaName);
                newMedicine.put("phoneNN",loggedInPhone);
                newMedicine.put("place", loggedInLocation);
                newMedicine.put("longitude",loggedInlong);
                newMedicine.put("lattitude",loggedInlatt);
                newMedicine.put("InventoryID", loggedInInventory);
                newMedicine.put("path",path);
                // Save medicineData to the database
                db.collection("MT_ADDEdMedicine").document(randomKey).set(newMedicine).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data successfully added with ID: randomKey
                        startActivity(new Intent(getApplicationContext(), AddMedecine.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error adding data
                    }
                });
            }

            private String generateRandomStringKey(int length) {
                String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
                Random random = new Random();
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < length; i++) {
                    int randomIndex = random.nextInt(characters.length());
                    char randomChar = characters.charAt(randomIndex);
                    sb.append(randomChar);
                }

                return sb.toString();
            }

        });


    }

    private void loadimage(String selectedBrand) {
        storageRef = storage.getReference().child("AdminImages/").child(selectedBrand);

        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageButtonMedicine);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });




    }

    private void decreaseQuantity() {
        int currentQuantity = Integer.parseInt(quantityEditText.getText().toString());

        // Decrease the quantity by 1 if it's greater than 0
        if (currentQuantity > 1) {
            currentQuantity--;

        }
        quantityEditText.setText(String.valueOf(currentQuantity));
    }

    private void increaseQuantity() {
        int currentQuantity = Integer.parseInt(quantityEditText.getText().toString());

        // Increase the quantity by 1
        currentQuantity++;


        // Update the TextView with the new value
        quantityEditText.setText(String.valueOf(currentQuantity));
    }



    private void getPharmacyShareData() {
        loggedInEmail = sharedPreferences.getString("loggedInEmail", "");
        loggedInlong=sharedPreferences.getString("longitude","");
        loggedInlatt=sharedPreferences.getString("lattitude","");
        loggedInInventory = sharedPreferences.getString("loggedInInventory", "");
        loggedInPhone = sharedPreferences.getString("loggedInPhone", "");
        loggedInPharmaName = sharedPreferences.getString("loggedInPharmaName", "");
        loggedInLocation = sharedPreferences.getString("loggedInLocation", "");


    }


    private void setBrandSpinnerValues() {
        List<String> brandList = new ArrayList<>();

        // Get a reference to the "MS_NewMedList" collection
        CollectionReference brandsRef = FirebaseFirestore.getInstance().collection("MT_MEDICINELIST");

        // Retrieve all documents in the "MS_NewMedList" collection
        brandsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG, "Number of documents retrieved: " + queryDocumentSnapshots.size());

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    // Get the Ingredients field value from each document and add it to the brandList ArrayList
                    String brandName = documentSnapshot.getId();
                    if (brandName != null) {
                        brandList.add(brandName);
                    }
                }

                // Set the brandSpinner adapter with the brandList ArrayList
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddMedecine.this, android.R.layout.simple_spinner_dropdown_item, brandList);
                brandSpinner.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: ", e);
            }
        });
    }

    public void goTohome(View view) {

        startActivity(new Intent(getApplicationContext(), DesignHomePharmacist.class));

    }

}
