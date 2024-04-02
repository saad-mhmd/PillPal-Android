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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pillpal_v2.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class AddProducts extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 123;
    private static final String TAG = "AddProducts";
    String loggedInEmail, loggedInInventory, loggedInPhone, loggedInPharmaName, loggedInLocation,loggedInlong,loggedInlatt;

    private CardView cardView;
    private ImageView imageButton;
    private EditText editTextName;
    private EditText editTextPrice;
    private EditText editTextQuantity;
    private EditText editTextDescription;
    private Button submitButton;
    EditText Quantity;
    Button plus, mins;
    String pathshare,pathF,pathE,imgName;


    private Uri imageUri;
    SharedPreferences sharedPreferences;
    private FirebaseFirestore firestore;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        sharedPreferences = getSharedPreferences("MyPharmaData", Context.MODE_PRIVATE);
        getPharmacyShareData();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        Quantity = findViewById(R.id.quantity_value);
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
        imageButton = findViewById(R.id.imageButtonproduct);
        editTextName = findViewById(R.id.editTextNameProduct);
        editTextPrice = findViewById(R.id.editTextPriceProduct);
        Quantity = findViewById(R.id.quantity_value);
        editTextDescription = findViewById(R.id.descriptionProduct);
        submitButton = findViewById(R.id.SubmitProduct);

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
                String quantity = Quantity.getText().toString();
                String description = editTextDescription.getText().toString();

                // Save user data in Firestore collection


                // Upload image to Firebase Cloud Storage
                uploadImage(name);
                saveUserData(name, price, quantity, description);
            }
        });
    }

    private void decreaseQuantity() {
        int currentQuantity = Integer.parseInt(Quantity.getText().toString());

        // Decrease the quantity by 1 if it's greater than 0
        if (currentQuantity > 1) {
            currentQuantity--;
        }

        // Update the TextView with the new value
        Quantity.setText(String.valueOf(currentQuantity));
    }

    private void increaseQuantity() {
        int currentQuantity = Integer.parseInt(Quantity.getText().toString());

        // Increase the quantity by 1
        currentQuantity++;


        // Update the TextView with the new value
        Quantity.setText(String.valueOf(currentQuantity));


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
        Map<String, Object> newProduct = new HashMap<>();
        newProduct.put("Name", name);
        newProduct.put("price", price);
        newProduct.put("pharmaphone",loggedInPhone);
        newProduct.put("phoneNN",loggedInPhone);
        newProduct.put("longitude",loggedInlong);
        newProduct.put("lattitude",loggedInlatt);
        newProduct.put("quantity", quantity);
        newProduct.put("PharmaName", loggedInPharmaName);
        newProduct.put("place", loggedInLocation);
        newProduct.put("description", description);
        newProduct.put("InventoryID", loggedInInventory);
        newProduct.put("path",pathF);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("MT_ADDedProduct").document(name).set(newProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Data successfully added with ID: name
                Log.d(TAG, "Data added successfully");
                Toast.makeText(AddProducts.this, "Data added successfully", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error adding data
                Log.e(TAG, "Error adding data", e);
                Toast.makeText(AddProducts.this, "Error adding data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImage(String imageName) {
        if (imageUri != null) {
            // Replace "images" with your desired folder name in Firebase Storage

            StorageReference imageRef = storageReference.child("ProductImages/").child(loggedInEmail).child(imageName);
            pathF="ProductImages/"+loggedInEmail+"/"+imageName;

          // uploadToPathCollection(pathshare);



            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Log.d(TAG, "Image uploaded successfully");
                        Toast.makeText(AddProducts.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddProducts.this, DesignHomePharmacist.class);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Image upload failed", e);
                        Toast.makeText(AddProducts.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void uploadToPathCollection(String pathshare) {
        Map<String, Object> newPath = new HashMap<>();
        newPath.put("path", pathshare);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("MT_ImagesPath").document(pathshare).set(newPath).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Data successfully added with ID: name
                Log.d(TAG, "Data added successfully");
                Toast.makeText(AddProducts.this, "Data added successfully", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error adding data
                Log.e(TAG, "Error adding data", e);
                Toast.makeText(AddProducts.this, "Error adding data", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
