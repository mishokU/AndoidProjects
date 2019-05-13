package com.example.polyfinderv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StartActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button createAnAccount;
    private Button signInButton;
    private Button registerButton;
    private boolean pushButton = false;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.start_activity);

        findAllViews();
        setOnAction();
    }

    private void findAllViews(){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password_text);
        signInButton = findViewById(R.id.sign_in_button);
        registerButton = findViewById(R.id.registered_button);
    }

    private void setOnAction(){

        signInButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                launchActivity(SignInActivity.class);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                launchActivity(CreateAccountActivity.class);
            }
        });
    }

    private void launchActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        overridePendingTransition(0,0);
    }
}
