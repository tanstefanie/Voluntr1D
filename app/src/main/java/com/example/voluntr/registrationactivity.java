package com.example.voluntr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class registrationactivity extends AppCompatActivity {
    private Button mRegister;
    private EditText mEmail,mPassword,mName;
    private RadioGroup mRadioGroup;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrationactivity);
        //CHECKS LOGIN
        mAuth = FirebaseAuth.getInstance();
        //IF USER IS LOGGED IN HE DOESN'T HAVE TO LOGIN AGAIN
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(registrationactivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
        mRegister = (Button) findViewById(R.id.register);

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mName = (EditText) findViewById(R.id.name);

        mRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int selectId = mRadioGroup.getCheckedRadioButtonId();

                final RadioButton radioButton = (RadioButton) findViewById(selectId);

                if (radioButton.getText() == null) {
                    return;
                }
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String Name = mName.getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(registrationactivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(registrationactivity.this, "Can't Sign Up", Toast.LENGTH_SHORT).show();
                        } else {
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance("https://voluntr-f211c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Users").child(radioButton.getText().toString()).child(userId).child("Name");
                            currentUserDb.setValue(Name);
                            Toast.makeText(registrationactivity.this, "registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}
