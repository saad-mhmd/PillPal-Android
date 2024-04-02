package com.example.pillpal_v2.PharmacyClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.pillpal_v2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class AddSuplement extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 123;
    private static final String TAG = "AddProducts";
    String loggedInEmail, loggedInInventory, loggedInPhone, loggedInPharmaName, loggedInLocation,loggedInlong,loggedInlatt;

    private CardView cardView;
    private ImageButton imageButton;
    private EditText editTextName;
    private EditText editTextPrice;
    private EditText editTextQuantity;
    private EditText editTextDescription;
    EditText editTextQuantitySup;
    private Button submitButton;
    Button plus, mins;


    private Uri imageUri;
    SharedPreferences sharedPreferences;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_suplement);
        editTextQuantitySup=findViewById(R.id.editTextQuantitySup);
        sharedPreferences = getSharedPreferences("MyPharmaData", Context.MODE_PRIVATE);
        getPharmacyShareData();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        plus = findViewById(R.id.quantity_plus);
        mins = findViewById(R.id.quantity_minus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseQuantity();

            }
        });
        mins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseQuantity();

            }
        });
        // Initialize views


        imageButton = findViewById(R.id.imageButtonSup);
        editTextName = findViewById(R.id.editTextNameSup);
        editTextPrice = findViewById(R.id.editTextPriceSup);
        editTextQuantity = findViewById(R.id.editTextQuantitySup);
        editTextDescription = findViewById(R.id.descriptionSup);
        submitButton = findViewById(R.id.SubmitSup);

        // Open gallery when image button is clicked
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // Save user data in collection and upload image when submit button is clicked
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String price = editTextPrice.getText().toString();
                String quantity = editTextQuantity.getText().toString();
                String description = editTextDescription.getText().toString();

                // Save user data in Firestore collection
                saveUserData(name, price, quantity, description);

                // Upload image to Firebase Cloud Storage
                uploadImage(name);
            }
        });
    }

    private void getPharmacyShareData() {
        loggedInEmail = sharedPreferences.getString("loggedInEmail", "");
        loggedInInventory = sharedPreferences.getString("loggedInInventory", "");
        loggedInlong=sharedPreferences.getString("longitude","");
        loggedInlatt=sharedPreferences.getString("lattitude","");
        loggedInPhone = sharedPreferences.getString("loggedInPhone", "");
        loggedInPharmaName = sharedPreferences.getString("loggedInPharmaName", "");
        loggedInLocation = sharedPreferences.getString("loggedInLocation", "");
    }
    private void decreaseQuantity() {
        int currentQuantity = Integer.parseInt(editTextQuantitySup.getText().toString());

        // Decrease the quantity by 1 if it's greater than 0
        if (currentQuantity > 1) {
            currentQuantity--;
        }

        // Update the TextView with the new value
        editTextQuantitySup.setText(String.valueOf(currentQuantity));
    }

    private void increaseQuantity() {
        int currentQuantity = Integer.parseInt(editTextQuantitySup.getText().toString());

        // Increase the quantity by 1
        currentQuantity++;


        // Update the TextView with the new value
        editTextQuantitySup.setText(String.valueOf(currentQuantity));


    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            imageButton.setImageURI(imageUri);
        }
    }

    private void saveUserData(String name, String price, String quantity, String description) {
        // Save user data in Firestore collection "products"
       String path= "SuplementImages/"+loggedInEmail+"/"+name;
        Map<String, Object> newProduct = new HashMap<>();
        newProduct.put("Name", name);
        newProduct.put("price", price);
        newProduct.put("longitude",loggedInlong);
        newProduct.put("lattitude",loggedInlatt);
        newProduct.put("quantity", quantity);
        newProduct.put("PharmaName", loggedInPharmaName);
        newProduct.put("phoneNN",loggedInPhone);
        newProduct.put("place", loggedInLocation);
        newProduct.put("description", description);
        newProduct.put("InventoryID", loggedInInventory);
        newProduct.put("path",path);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("MT_ADDedSuplement").document(name).set(newProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Data successfully added with ID: name
                Log.d(TAG, "Data added successfully");
                Toast.makeText(AddSuplement.this, "Data added successfully", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error adding data
                Log.e(TAG, "Error adding data", e);
                Toast.makeText(AddSuplement.this, "Error adding data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImage(String imageName) {
        if (imageUri != null) {
            // Replace "images" with your desired folder name in Firebase Storage
            StorageReference imageRef = storageReference.child("SuplementImages/").child(loggedInEmail).child(imageName);
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Log.d(TAG, "Image uploaded successfully");
                        Toast.makeText(AddSuplement.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddSuplement.this, DesignHomePharmacist.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Image upload failed", e);
                        Toast.makeText(AddSuplement.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    });

    }
    }
}