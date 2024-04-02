package com.example.pillpal_v2.PatientClasses;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pillpal_v2.Model.SearchAdapter;
import com.example.pillpal_v2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CustomerHomeActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private static final String TAG = "CustomerHomeActivity";
    String lowercaseDocumentId;
    CollectionReference categoryRef ;
    Spinner categorySpinner;
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        db = FirebaseFirestore.getInstance();


        Button searchButton = findViewById(R.id.search_button);
        recyclerView = findViewById(R.id.results_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.GONE);

        //Button viewdocument=findViewById(R.id.view_document_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText searchBar = findViewById(R.id.search_bar);
                String searchQuery = searchBar.getText().toString().trim();
                // Convert the search query to lowercase for case-insensitive comparison
                String lowercaseSearchQuery = searchQuery.toLowerCase();
                // Create a Firestore query that searches for documents whose IDs containthe search query (case-insensitive)
                Query query = db.collection("MT_MEDICINELIST")
                        .orderBy(FieldPath.documentId())
                        .startAt(lowercaseSearchQuery)
                        .endAt(lowercaseSearchQuery + "\uf8ff");
                // Execute the query and handle the search results
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> matchingDocumentIds = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Convert the document ID to lowercase for case-insensitive comparison
                                lowercaseDocumentId = document.getId().toLowerCase();
                                // Check if the lowercase document ID contains the lowercase search query
                                if (lowercaseDocumentId.contains(lowercaseSearchQuery)) {
                                    matchingDocumentIds.add(document.getId());
                                }
                            }
                            if (matchingDocumentIds.isEmpty()) {
                                searchAdapter = new SearchAdapter(new ArrayList<>());
                                recyclerView.setAdapter(searchAdapter);
                                recyclerView.setVisibility(View.GONE);
                                // Create a message to display based on the search results
                                String message = "No results found";
                                // Display the message in a TextView
                                TextView messageView = findViewById(R.id.message_view);
                                messageView.setText(message);
                            } else {
                                searchAdapter = new SearchAdapter(matchingDocumentIds);
                                recyclerView.setAdapter(searchAdapter);
                                recyclerView.setVisibility(View.VISIBLE);
                                // Create a message to display based on the search results
                                String message = "Found " + matchingDocumentIds.size() + " result(s)";
                                // Display the message in a TextView
                                TextView messageView = findViewById(R.id.message_view);
                                messageView.setText(message);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
            }
        });
    }


    public void documentInfo(View view) {
    }
}