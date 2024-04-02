package com.example.pillpal_v2.SignInSignup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pillpal_v2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Register extends AppCompatActivity {
    private EditText registerName;
    private EditText phoneNumber;
    private EditText registerEmail;
    private EditText registerPassword;
    private CheckBox isPharma;
    private CheckBox isPatient;
    private Button registerBtn;
    boolean valid = true;
    DocumentReference df, dd;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String firstTimeLogin;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        registerName = findViewById(R.id.registerName);
        phoneNumber = findViewById(R.id.phoneNumber);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        isPharma = findViewById(R.id.isPharma);
        isPatient = findViewById(R.id.isPatient);
        registerBtn = findViewById(R.id.registerBtn);
        TextView goToLogin = findViewById(R.id.gotoLogin);
        isPharma = findViewById(R.id.isPharma);
        isPatient = findViewById(R.id.isPatient);
        registerBtn = findViewById(R.id.registerBtn);
        //goToLogin = findViewById(R.id.goToLoginn);
        isPharma = findViewById(R.id.isPharma);
        isPatient = findViewById(R.id.isPatient);

        isPharma.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                isPatient.setChecked(false);
            }
        });

        isPatient.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                isPharma.setChecked(false);
            }
        });
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
        registerBtn.setOnClickListener(view -> {
            valid = true; // reset valid flag
            checkField(registerName);
            checkField(registerEmail);
            checkField(registerPassword);
            checkField(phoneNumber);

            // Radio group validation
            if (!(isPharma.isChecked() || isPatient.isChecked())) {
                Toast.makeText(Register.this, "Select the account type", Toast.LENGTH_SHORT).show();
                return;
            }

            if (valid) {
                // Start user registration process
                fAuth.createUserWithEmailAndPassword(registerEmail.getText().toString(), registerPassword.getText().toString()).addOnSuccessListener(authResult -> {
                    FirebaseUser user = fAuth.getCurrentUser();
                    user.sendEmailVerification().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Verification email sent. Check your email.", Toast.LENGTH_SHORT).show();

                            // Save user data to Firestore after the email is verified
                            user.reload();
                            if (isPharma.isChecked()) {
                                String randomKey = generateRandomStringKey(8);


                                editor.putString("randomKey", randomKey);
                                editor.apply();
                                df = fStore.collection("PharmaUsers").document(registerEmail.getText().toString());
                                dd = fStore.collection("users").document(registerEmail.getText().toString());
                                Map<String, Object> userInfo = new HashMap<>();
                                userInfo.put("Fullname", registerName.getText().toString());
                                userInfo.put("UserEmail", registerEmail.getText().toString());
                                userInfo.put("PhoneNumber", phoneNumber.getText().toString());
                                firstTimeLogin = "1";
                                userInfo.put("first time login ", firstTimeLogin);
                                userInfo.put("Inventory_ID", randomKey);

                                // Specify if the user is admin
                                userInfo.put("isPharma", "1");
                                userInfo.put("isPatient", "0");
                                df.set(userInfo);
                                dd.set(userInfo);
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                                finish();


                            } else if (isPatient.isChecked()) {
                                df = fStore.collection("PatientUsers").document(registerEmail.getText().toString());
                                dd = fStore.collection("users").document(registerEmail.getText().toString());

                                Map<String, Object> userInfo = new HashMap<>();
                                userInfo.put("Fullname", registerName.getText().toString());
                                userInfo.put("UserEmail", registerEmail.getText().toString());
                                userInfo.put("PhoneNumber", phoneNumber.getText().toString());
                                firstTimeLogin = "1";
                                userInfo.put("first time login ", firstTimeLogin);

                                // Specify if the user is admin
                                userInfo.put("isPharma", "0");
                                userInfo.put("isPatient", "1");
                                df.set(userInfo);
                                dd.set(userInfo);
                                dd.set(userInfo);
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                                finish();


                            }
                            finish();
                        } else {
                            Toast.makeText(Register.this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                        }
                    });
                }).addOnFailureListener(e -> Toast.makeText(Register.this, "Registration failed. " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

        ;
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

    public boolean checkField(EditText textField) {
        if (textField.getText().toString().isEmpty()) {
            textField.setError("Error");
            valid = false;
        } else {
            valid = true;
        }
        return valid;
    }
}