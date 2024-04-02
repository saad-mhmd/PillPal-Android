package com.example.pillpal_v2.SignInSignup;

import android.content.Intent;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pillpal_v2.PatientClasses.PatientHomePage;
import com.example.pillpal_v2.PatientClasses.PatientInfo;
import com.example.pillpal_v2.PharmacyClasses.DesignHomePharmacist;
import com.example.pillpal_v2.PharmacyClasses.PharmaInfo;

import com.example.pillpal_v2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private TextInputLayout emailw;
    private TextInputLayout passwordw;
    private Button loginBtn;
    private TextView gotoRegister, forgotpassword;
    private SharedPreferences sharedPreferenceslogin,sharedPreferences;



    private boolean valid = true;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String isPharma, isPatient, firstTimeLogin;
    private DocumentReference dd, df;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        gotoRegister = findViewById(R.id.gotoRegister);
        emailw = findViewById(R.id.textInputEmail);
        passwordw = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);

        forgotpassword = findViewById(R.id.forget_password);
        sharedPreferences = getSharedPreferences("PillPalPrefs", MODE_PRIVATE);
        sharedPreferenceslogin = getSharedPreferences("PREF_NAMEL", MODE_PRIVATE);

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailw.getEditText().getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    emailw.setError("Please enter your email");
                } else {
                    fAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Login.this, "Reset password link sent to your email", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailw.getEditText().getText().toString().trim();
                String password = passwordw.getEditText().getText().toString().trim();

                checkField(emailw.getEditText());
                checkField(passwordw.getEditText());

                if (valid) {
                    fAuth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    FirebaseUser user = authResult.getUser();
                                    if (user.isEmailVerified()) {
                                        checkUserAccessLevel(user.getUid());
                                    } else {
                                        Toast.makeText(Login.this, "Please verify your email address", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }

    private void checkUserAccessLevel(String uid) {
        df = fStore.collection("users").document(emailw.getEditText().getText().toString());

        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        isPharma = documentSnapshot.getString("isPharma");
                        isPatient = documentSnapshot.getString("isPatient");
                        firstTimeLogin = documentSnapshot.getString("first time login ");

                        if ("1".equals(firstTimeLogin)) {
                            dd = fStore.collection("users").document(emailw.getEditText().getText().toString());
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("first time login", "0");
                            dd.update(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    if ("1".equals(isPharma)) {
                                        startActivity(new Intent(getApplicationContext(), PharmaInfo.class));
                                        finish();
                                    } else if ("1".equals(isPatient)) {
                                        startActivity(new Intent(getApplicationContext(), PatientInfo.class));
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Login.this, "Error updating user data", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if ("1".equals(isPatient)) {
                            startActivity(new Intent(getApplicationContext(), PatientHomePage.class));
                            finish();
                        } else if ("1".equals(isPharma)) {
                            startActivity(new Intent(getApplicationContext(), DesignHomePharmacist.class));
                            finish();
                        } else {
                            Toast.makeText(Login.this, "Unknown user access level", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Login.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, "Error retrieving user data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    private boolean checkField(EditText textField) {
        String value = textField.getText().toString().trim();

        if (TextUtils.isEmpty(value)) {
            textField.setError("Required");
            valid = false;
        } else {
            textField.setError(null);
            valid = true;
        }

        return valid;
    }
}
