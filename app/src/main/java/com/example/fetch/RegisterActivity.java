package com.example.fetch;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

import android.util.Log;

import android.app.LoaderManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Abdullah on 9/9/17.
 */

public class RegisterActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    private String username;
    private String useremail;

    private AutoCompleteTextView nameInput;
    private AutoCompleteTextView emailInput;

    private EditText password;
    private EditText repassword;

    private String TAG = "CreateAccount";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_register);
        //TextFields
        nameInput = (AutoCompleteTextView) findViewById(R.id.register_name);
        emailInput = (AutoCompleteTextView) findViewById(R.id.register_email);

        password = (EditText) findViewById(R.id.register_password);
        repassword = (EditText) findViewById(R.id.register_password_conform);

        //Buttons
        Button to_last_actvity = (Button) findViewById(R.id.go_back_button);
        Button new_user = (Button) findViewById(R.id.new_register);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Going Back to last activity
        to_last_actvity.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        //Creating New User
        new_user.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInput.getText().toString();
                String createpassword = password.getText().toString();
                createAccount(email, createpassword);

            }
        });
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(RegisterActivity.this, "Authentication Succseded.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

                            username = nameInput.getText().toString();
                            useremail = emailInput.getText().toString();
                            String userID = currentFirebaseUser.getUid();

                            User u = new User(useremail, username);
                            //Log.d("Database", "Username:"  + u.name + " useremail:" + u.email);
                            mDatabase.child("users").child(userID).setValue(u);

                            //
                            Intent i = new Intent(getApplicationContext(),OpeningActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                });
        // [END create_user_with_email]
    }

}
