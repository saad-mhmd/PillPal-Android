package com.example.pillpal_v2.PatientClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.AlarmClock;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pillpal_v2.PharmacyClasses.Showexperts;
import com.example.pillpal_v2.R;
import com.example.pillpal_v2.SignInSignup.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Random;

public class PatientHomePage extends AppCompatActivity {
    // Declare quote-related variables
    private FirebaseAuth firebaseAuth;

    private String[] dailyHealthQuotes = {
            "Take care of your body; it's the only place you have to live.",
            "Health is a state of complete harmony of the body, mind, and spirit.",
            "Good health and good sense are two of life's greatest blessings.",
            "Your health is an investment, not an expense.",
            "The greatest wealth is health."
    };
    private FirebaseAuth fAuth;
    FirebaseUser user;
    String Fullname;
    TextView welcomeName;
    private SharedPreferences sharedPreferences, logSharePref;
    private static final String PREF_NAME = "DailyHealthQuotes";
    private static final String KEY_QUOTE_INDEX = "quoteIndex";
    String loggedInEmail;
    // Declare handler for resetting quotes
    private Handler handler = new Handler();
    ImageView homeIcon, compareIcon, trackIcon, reminderIcon, settingsIcon,searchicon;
    ImageView box1, box2, box3, box4, box5, box6, box7, box8;
    TextView textViewName1, textViewName2, textViewName3, textViewName4, textViewName5, textViewName6, textViewName7, textViewName8;
    TextView textViewPrice1, textViewPrice2, textViewPrice3, textViewPrice4, textViewPrice5, textViewPrice6, textViewPrice7, textViewPrice8;
    TextView dailyqoute;
    DocumentReference df;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    ImageView category1, category2, category3, category4;
    String[] places = new String[8];
    String[] names = new String[8];
    String[] prices = new String[8];
    String[] paths = new String[8];
    String[] longe=new String[8];
    String[] latt=new String[8];
    String[] PharmaNames = new String[8];
    String[] descriptions = new String[8];
    String[] phoneNumbers = new String[8];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.design_home_patient);
        welcomeName = findViewById(R.id.welcomeName);
        dailyqoute = findViewById(R.id.daily_qoute);
        searchicon=findViewById(R.id.searchIcon);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        sharedPreferences = getSharedPreferences("logSharePrefd", MODE_PRIVATE);

        settingsIcon = findViewById(R.id.settingsIcon);
        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
        category1 = findViewById(R.id.category1);
        category2 = findViewById(R.id.category2);
        category3 = findViewById(R.id.category3);
        category4 = findViewById(R.id.category4);
        category4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientHomePage.this, Showexperts.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        category3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(PatientHomePage.this, ShowSupplementPatient.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        category2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientHomePage.this, ShowProdcutPatient.class);

                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
        category1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientHomePage.this, ShowMedPatient.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
        box1 = findViewById(R.id.box1);
        box2 = findViewById(R.id.box2);
        box3 = findViewById(R.id.box3);
        box4 = findViewById(R.id.box4);
        box5 = findViewById(R.id.box5);
        box6 = findViewById(R.id.box6);
        box7 = findViewById(R.id.box7);
        box8 = findViewById(R.id.box8);
        box2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = 1;
                gotoproductinfo(index);

            }
        });
        box3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = 2;
                gotoproductinfo(index);

            }
        });
        box4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = 3;
                gotoproductinfo(index);

            }
        });
        box5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = 4;
                gotoproductinfo(index);

            }
        });
        box6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = 5;
                gotoproductinfo(index);

            }
        });
        box7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = 6;
                gotoproductinfo(index);

            }
        });
        box8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = 7;
                gotoproductinfo(index);

            }
        });
        box1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int index = 0;
                gotoproductinfo(index);
             /*   Intent intent = new Intent(PatientHomePage.this, ProductsInfo.class);
                String productName = names[0];
                String pharmacyName=PharmaNames[0];
                String pharmaplace=places[0];
                String productPrice=prices[0];
                String productdescription=descriptions[0];
                String productspath=paths[0];

                intent.putExtra("medicineName", productName);
                intent.putExtra("pharmacyName", pharmacyName);
                intent.putExtra("pharmaplace", pharmaplace);
                intent.putExtra("productPrice", productPrice);
                intent.putExtra("productspath", productspath);
                intent.putExtra("productdescription", productdescription);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            */
            }

        });

        textViewName1 = findViewById(R.id.box1Name);
        textViewName2 = findViewById(R.id.box2Name);
        textViewName3 = findViewById(R.id.box3Name);
        textViewName4 = findViewById(R.id.box4Name);
        textViewName5 = findViewById(R.id.box5Name);
        textViewName6 = findViewById(R.id.box6Name);
        textViewName7 = findViewById(R.id.box7Name);
        textViewName8 = findViewById(R.id.box8Name);

        textViewPrice1 = findViewById(R.id.box1Price);
        textViewPrice2 = findViewById(R.id.box2Price);
        textViewPrice3 = findViewById(R.id.box3Price);
        textViewPrice4 = findViewById(R.id.box4Price);
        textViewPrice5 = findViewById(R.id.box5Price);
        textViewPrice6 = findViewById(R.id.box6Price);
        textViewPrice7 = findViewById(R.id.box7Price);
        textViewPrice8 = findViewById(R.id.box8Price);

        homeIcon = findViewById(R.id.homeIcon);
        compareIcon = findViewById(R.id.compareIcon);
        trackIcon = findViewById(R.id.trackIcon);
        reminderIcon = findViewById(R.id.reminderIcon);


        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        if (user != null) {
            loggedInEmail = user.getEmail(); // Retrieve the email address
        }

        df = db.collection("PatientUsers").document(loggedInEmail);

        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Fullname = documentSnapshot.getString("Fullname");
                        welcomeName.setText(Fullname);    // Save the values in shared preferences
                    }
                }
            }
        });


        loadImages();
        loadDailyHealthQuotes();


        homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientHomePage.this, PatientHomePage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientHomePage.this, CustomerHomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        compareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientHomePage.this, Compare2.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        trackIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setClassName("com.android.deskclock", "com.android.deskclock.DeskClock");
                try {
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } catch (ActivityNotFoundException e) {
                    // Clock application is not installed on the device
                    Toast.makeText(PatientHomePage.this, "Clock application not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reminderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    // Clock application is not installed on the device or does not have alarms
                    Toast.makeText(v.getContext(), "Clock application or alarm functionality not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.menu_bar, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_profile:
                        // Handle profile click
                        Toast.makeText(PatientHomePage.this, "Profile clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_about_us:
                        // Handle about us click
                        Toast.makeText(PatientHomePage.this, "About Us clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_credits:
                        // Handle credits click
                        Toast.makeText(PatientHomePage.this, "Credits clicked", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.menu_logout:
                        // Handle logout click
                        firebaseAuth.signOut();
                        startLoginActivity();
                        Toast.makeText(PatientHomePage.this, "Logout clicked", Toast.LENGTH_SHORT).show();

                        return true;
                    default:
                        return false;
                }
            }
        });

        popupMenu.show();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(PatientHomePage.this, Login.class);
        startActivity(intent);
        finish();
    }


    private void gotoproductinfo(int index) {
        Intent intent = new Intent(PatientHomePage.this, ProductsInfo.class);
        String productName = names[index];
        String pharmacyName = PharmaNames[index];
        String pharmaplace = places[index];
        String longee=longe[index];
        String lattt=latt[index];
        String productPrice = prices[index];
        String productdescription = descriptions[index];
        String productspath = paths[index];
        String phoneNumber = phoneNumbers[index];
        String indexx = "box";
        intent.putExtra("index", indexx);
        intent.putExtra("medicineName", productName);
        intent.putExtra("pharmacyName", pharmacyName);
        intent.putExtra("long",longee);
        intent.putExtra("latt",lattt);
        intent.putExtra("pharmaplace", pharmaplace);
        intent.putExtra("productPrice", productPrice);
        intent.putExtra("productspath", productspath);
        intent.putExtra("phone", phoneNumber);
        intent.putExtra("productdescription", productdescription);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void loadDailyHealthQuotes() {
        // Initialize shared preferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Retrieve the current quote index from shared preferences
        int quoteIndex = sharedPreferences.getInt(KEY_QUOTE_INDEX, -1);

        // Check if the quote index is valid
        if (quoteIndex == -1 || quoteIndex >= dailyHealthQuotes.length) {
            // Reset the quote index if invalid
            resetDailyHealthQuote();
            quoteIndex = 0;
        }

        // Retrieve the daily quote based on the current quote index
        String dailyQuoteText = dailyHealthQuotes[quoteIndex];

        // Display the daily quote
        dailyqoute.setText(dailyQuoteText);


    }

    private void resetDailyHealthQuote() {
        // Generate a random quote index
        Random random = new Random();
        int quoteIndex = random.nextInt(dailyHealthQuotes.length);

        // Save the quote index to shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_QUOTE_INDEX, quoteIndex);
        editor.apply();
    }

    private void loadImages() {
        db.collection("MT_ADDedProduct")
                .limit(8)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //   Collections.shuffle(documents); // Shuffle the documents

                        int index = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String imagePath = document.getString("path");
                            String name = document.getString("Name");
                            String price = document.getString("price");
                            String lon1=document.getString("longitude");
                            String lat1=document.getString("lattitude");
                            String place = document.getString("place");
                            String description = document.getString("description");
                            String PharmaName = document.getString("PharmaName");
                            String phone = document.getString("pharmaphone");
                        //    descriptions[index] = description;
                            places[index] = place;
                            descriptions[index]=description;
                            latt[index]=lat1;
                            longe[index]=lon1;
                            phoneNumbers[index] = phone;
                            PharmaNames[index] = PharmaName;
                            prices[index] = price;
                            names[index] = name;
                            paths[index] = imagePath;

                            loadImageIntoImageView(imagePath, name, price, index);
                            index++;
                        }
                    } else {
                        Toast.makeText(PatientHomePage.this, "Error fetching image paths", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadImageIntoImageView(String imagePath, String name, String price, int index) {
        StorageReference imageRef = storage.getReference().child(imagePath);
        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            ImageView targetImageView = getImageViewByIndex(index);
            TextView targetNameTextView = getTextViewNameByIndex(index);
            TextView targetPriceTextView = getTextViewPriceByIndex(index);

            if (targetImageView != null) {
                Picasso.get().load(uri).into(targetImageView);
            }
            if (targetNameTextView != null) {
                targetNameTextView.setText(name);
            }
            if (targetPriceTextView != null) {
                targetPriceTextView.setText(price);
            }
        }).addOnFailureListener(e -> Toast.makeText(PatientHomePage.this, "Error loading image", Toast.LENGTH_SHORT).show());
    }

    private void startQuoteReset() {
        // Schedule the initial quote reset after 24 hours
        handler.postDelayed(resetQuoteRunnable, 10 * 1000);
    }

    private void stopQuoteReset() {
        // Stop the quote reset process (for example, in onDestroy or onPause)
        handler.removeCallbacks(resetQuoteRunnable);
    }

    private Runnable resetQuoteRunnable = new Runnable() {
        @Override
        public void run() {
            // Reset the daily quote and update the UI
            resetDailyHealthQuote();
            loadDailyHealthQuotes();

            // Schedule the next quote reset after 10
            handler.postDelayed(resetQuoteRunnable, 10 * 1000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the quote reset process when the activity is destroyed
        stopQuoteReset();
    }

    private ImageView getImageViewByIndex(int index) {
        switch (index) {
            case 0:
                return box1;
            case 1:
                return box2;
            case 2:
                return box3;
            case 3:
                return box4;
            case 4:
                return box5;
            case 5:
                return box6;
            case 6:
                return box7;
            case 7:
                return box8;
            default:
                return null;
        }
    }

    private TextView getTextViewNameByIndex(int index) {
        switch (index) {
            case 0:
                return textViewName1;
            case 1:
                return textViewName2;
            case 2:
                return textViewName3;
            case 3:
                return textViewName4;
            case 4:
                return textViewName5;
            case 5:
                return textViewName6;
            case 6:
                return textViewName7;
            case 7:
                return textViewName8;
            default:
                return null;
        }
    }

    private TextView getTextViewPriceByIndex(int index) {
        switch (index) {
            case 0:
                return textViewPrice1;
            case 1:
                return textViewPrice2;
            case 2:
                return textViewPrice3;
            case 3:
                return textViewPrice4;
            case 4:
                return textViewPrice5;
            case 5:
                return textViewPrice6;
            case 6:
                return textViewPrice7;
            case 7:
                return textViewPrice8;
            default:
                return null;
        }
    }
}