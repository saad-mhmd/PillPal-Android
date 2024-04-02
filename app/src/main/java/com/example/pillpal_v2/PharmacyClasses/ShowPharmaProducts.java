package com.example.pillpal_v2.PharmacyClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.pillpal_v2.Model.Product;
import com.example.pillpal_v2.Model.ProductAdapter;
import com.example.pillpal_v2.R;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShowPharmaProducts extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String loggedInEmail, loggedInInventory, loggedInPhone, loggedInPharmaName, loggedInLocation;

    private static final String TAG = "ShowPharmaProducts";
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    Button backProd;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pharma_products);
        sharedPreferences = getSharedPreferences("MyPharmaData", Context.MODE_PRIVATE);
        getPharmacyShareData();






















                backProd = findViewById(R.id.backProd);
        backProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(productAdapter);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        loadData();
    }

    private void getPharmacyShareData() {
        loggedInEmail = sharedPreferences.getString("loggedInEmail", "");
        loggedInInventory = sharedPreferences.getString("loggedInInventory", "");
        loggedInPhone = sharedPreferences.getString("loggedInPhone", "");
        loggedInPharmaName = sharedPreferences.getString("loggedInPharmaName", "");
        loggedInLocation = sharedPreferences.getString("loggedInLocation", "");

    }

    private void loadData() {
        firestore.collection("MT_ADDedProduct").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String Inv = document.getString("InventoryID");
                    //Toast.makeText(ShowSuplement.this,Inv,Toast.LENGTH_SHORT).show();
                    if (loggedInInventory.equals(Inv)) {
                        String name = document.getString("Name");
                        String price = document.getString("price");
                        String quantity = document.getString("quantity");
                        String description = document.getString("description");

                        String documentId = document.getId(); // Get the document ID
                        String path1="ProductImages/"+loggedInEmail+"/"+documentId;
                        StorageReference imageRef = storage.getReference().child(path1);
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();

                            Product product = new Product(name, price, quantity, description, imageUrl,documentId);
                            productList.add(product);
                            productAdapter.notifyDataSetChanged();
                        });
                        imageRef.getDownloadUrl().addOnFailureListener(e -> {
                            String errorMessage = e.getMessage();
                            Log.e(TAG, "Error getting image URL: " + errorMessage, e);
                        });
                    }
                }
            } else {
                Log.e(TAG, "Error getting documents", task.getException());
            }
        });
    }

}
