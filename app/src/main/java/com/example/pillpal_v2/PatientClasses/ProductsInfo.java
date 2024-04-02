package com.example.pillpal_v2.PatientClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pillpal_v2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProductsInfo extends AppCompatActivity {
    private FirebaseStorage storage;
    String phone1;
    FirebaseFirestore fstore;
    DocumentReference df, dm;
    String des;
    String longitude, lattitude;
    String productdescription;
    String ppdd;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_info);
        fstore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        ImageView imageView = findViewById(R.id.imageView);
        ImageView mapview = findViewById(R.id.mapviewpharma);
        TextView longlatt = findViewById(R.id.longlatt);
        TextView medicineNameTextView = findViewById(R.id.medicineNameTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        TextView pharmacyNameTextView = findViewById(R.id.pharmacyNameTextView);
        TextView productprice = findViewById(R.id.priceTextView);
        TextView placeTextView = findViewById(R.id.placeTextView);
        TextView phonen = findViewById(R.id.phonennumber);
        Intent intent = getIntent();
        String index = intent.getStringExtra("index");

        String productName = intent.getStringExtra("medicineName");


        String pharmacyName = intent.getStringExtra("pharmacyName");
        String documentID = intent.getStringExtra("documentID");
        String pharmaplace = intent.getStringExtra("pharmaplace");
        String productPrice = intent.getStringExtra("productPrice");
        String phone = intent.getStringExtra("phone");
        String lomg=intent.getStringExtra("long");
        String latin=intent.getStringExtra("latt");
        String productspath = intent.getStringExtra("productspath");
        String camp=lomg+","+latin;
        longlatt.setText(camp);
         ppdd = intent.getStringExtra("productdescription");
        productdescription = intent.getStringExtra("description");


        mapview.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                if(index.equals("box")){String geoUri = "geo:" + lomg + "," + latin;
                    Uri mapUri = Uri.parse(geoUri);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                    startActivity(mapIntent);
                    mapIntent.setPackage("com.google.android.apps.maps");}
                else {
                    double l = Double.parseDouble(longitude);
                    //   double latitudee=lastLocation.getLatitude();
                    //     double longitudee=lastLocation.getLongitude();
                    double m = Double.parseDouble(lattitude);

                    String geoUri = "geo:" + l + "," + m;
                    Uri mapUri = Uri.parse(geoUri);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                    startActivity(mapIntent);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    //  if (mapIntent.resolveActivity(getPackageManager()) != null) {

                    //    }\

                }}
        });

        if (index.equals("medicine")) {
            dm = fstore.collection("MT_ADDEdMedicine").document(documentID);
            dm.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot1 = task.getResult();
                        if (documentSnapshot1.exists()) {
                            phone1 = documentSnapshot1.getString("phoneNN");
                            longitude = intent.getStringExtra("longitude");
                            lattitude = intent.getStringExtra("lattitude");
                            String comp = longitude + "" + lattitude;
                            longlatt.setText(comp);

                            phonen.setText(phone1);
                        }

                    }
                }
            });


            df = fstore.collection("MT_MEDICINELIST").document(productName);
            df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            des = documentSnapshot.getString("description");
                            String longitude = intent.getStringExtra("longitude");
                            String lattitude = intent.getStringExtra("lattitude");

                            medicineNameTextView.setText(productName);

                            placeTextView.setText(pharmaplace);
                            pharmacyNameTextView.setText(pharmacyName);
                            productprice.setText(productPrice);
                            descriptionTextView.setText(des);
                            String comp = longitude + "" + lattitude;
                            longlatt.setText(comp);

                        }
                    }
                }
            });


        } else if (index.equals("product")) {
            df = fstore.collection("MT_ADDedProduct").document(productName);
            df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            String phoneproduct = documentSnapshot.getString("pharmaphone");
                            medicineNameTextView.setText(productName);
                            longitude = intent.getStringExtra("longitude");
                            String producdesc = documentSnapshot.getString("description");
                            lattitude = intent.getStringExtra("lattitude");
                            String comp = longitude + "" + lattitude;
                            longlatt.setText(comp);

                            placeTextView.setText(pharmaplace);
                            pharmacyNameTextView.setText(pharmacyName);
                            productprice.setText(productPrice);
                            phonen.setText(phoneproduct);
                            descriptionTextView.setText(producdesc);
                        }
                    }
                }
            });
        } else if (index.equals("suplement")) {
            df = fstore.collection("MT_ADDedSuplement").document(productName);
            df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            String supname = documentSnapshot.getString("Name");
                            String pharmaname = documentSnapshot.getString("PharmaName");
                            String supplace = documentSnapshot.getString("place");
                            String supprice = documentSnapshot.getString("price");
                            String desc = documentSnapshot.getString("description");
                            String pp = documentSnapshot.getString("pharmaphone");
                            longitude = intent.getStringExtra("longitude");
                            lattitude = intent.getStringExtra("lattitude");
                            String comp = longitude + "" + lattitude;
                            longlatt.setText(comp);

                            medicineNameTextView.setText(productName);

                            placeTextView.setText(supname);
                            pharmacyNameTextView.setText(pharmaname);
                            productprice.setText(supprice);
                            placeTextView.setText(supplace);
                            descriptionTextView.setText(ppdd);
                            phonen.setText(pp);
                        }
                    }
                }
            });
        } else {


            phonen.setText(phone);
            medicineNameTextView.setText(productName);
            descriptionTextView.setText(ppdd);
            placeTextView.setText(pharmaplace);
            pharmacyNameTextView.setText(pharmacyName);
            productprice.setText(productPrice);
            loadimage(imageView, productspath);


        }


        ImageView call = findViewById(R.id.phonecall);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(phonen.getText().toString().isEmpty())) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phonen.getText().toString()));
                    startActivity(intent);
                } else {
                    // Handle case when no phone number is entered
                    Toast.makeText(getApplicationContext(), "Please enter a phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadimage(imageView, productspath);
    }

    private void loadimage(ImageView imageView, String productspath) {
        StorageReference imageRef = storage.getReference().child(productspath);
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(imageView);


        }).addOnFailureListener(e -> Toast.makeText(ProductsInfo.this, "Error loading image", Toast.LENGTH_SHORT).show());
    }
}