package com.example.pillpal_v2;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pillpal_v2.PharmacyClasses.DesignHomePharmacist;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;
import java.util.Map;

public class TestLocation extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    double latitude;
    protected GoogleApiClient googleApiClient;
    protected Location lastLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    protected TextView longitudeText, latitudeText;
    public static final int RequestPermissionCode = 1;
    private FirebaseFirestore db, dm;
    String name ,email,phone,address,medicalInfo,longe,latt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_location);
        db=FirebaseFirestore.getInstance();
        dm= FirebaseFirestore.getInstance();
        Intent intent=getIntent();
         name = intent.getStringExtra("pharmaname");
         email = intent.getStringExtra("pharmaemail");
         phone = intent.getStringExtra("pharmaphone");
         address = intent.getStringExtra("pharmaaddress");
         medicalInfo = intent.getStringExtra("pharmamedical");



        longitudeText = findViewById(R.id.longitude_text);
        latitudeText =  findViewById(R.id.latitude_text);

        Button test = findViewById(R.id.testlocation);
        test.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("QueryPermissionsNeeded")
            @Override
            public void onClick(View view) {
                uploaddata(name,email,phone,address,medicalInfo,longe,latt);

            /*    double l = Double.parseDouble(longitudeText.getText().toString());
             //   double latitudee=lastLocation.getLatitude();
           //     double longitudee=lastLocation.getLongitude();
                double m = Double.parseDouble(latitudeText.getText().toString());

                String geoUri = "geo:" + m + "-" +l;
                Uri mapUri = Uri.parse(geoUri);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
                startActivity(mapIntent);
                mapIntent.setPackage("com.google.android.apps.maps");
              //  if (mapIntent.resolveActivity(getPackageManager()) != null) {

                //    }
              */  }


        });

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void uploaddata(String name, String email, String phone, String address, String medicalInfo, String longe, String latt) {

        Map<String, Object> PatientsInfo = new HashMap<>();
        PatientsInfo.put("pharmaName", name);
        PatientsInfo.put("phone", phone);
        PatientsInfo.put("place", address);
        PatientsInfo.put("medical info", medicalInfo);
        PatientsInfo.put("longitude", longe);
        PatientsInfo.put("lattitude", latt);
        // Add data to Firestore
        db.collection("pharmacyInfo").document(email)
                .set(PatientsInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "pharmacy  Info added successfully.", Toast.LENGTH_SHORT).show();
                        firsttimelogin(email);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error adding Pharmacy Info. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });





    }

    private void firsttimelogin(String email) {
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

        startActivity(new Intent(TestLocation.this, DesignHomePharmacist.class));
        finish();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(TestLocation.this, new
                String[]{ACCESS_FINE_LOCATION}, RequestPermissionCode);
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermission();
        } else {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                latitudeText.setText(String.valueOf(location.getLatitude()));
                                longitudeText.setText(String.valueOf(location.getLongitude()));
                                longe=longitudeText.getText().toString();
                                latt=latitudeText.getText().toString();
                            }
                        }
                    });
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("MainActivity", "Connection suspended : ");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.e("MainActivity", "Connection failed : " + connectionResult.getErrorCode());

    }
}