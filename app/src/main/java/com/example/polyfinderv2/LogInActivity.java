package com.example.polyfinderv2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LogInActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private TextView createAnAccount;
    private Button signInButton;
    private boolean pushButton = false;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.log_in_activity);

        findAllViews();
        setOnAction();
    }

    private void findAllViews(){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        createAnAccount = findViewById(R.id.createAnAccount);
        signInButton = findViewById(R.id.sign_in_button);
    }

    private void setOnAction(){

        signInButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(checkData()){
                    launchActivity(MainActivity.class);
                } else {
                    Toast incorrect = Toast.makeText(getApplicationContext(), "Incorrect data", Toast.LENGTH_LONG);
                    incorrect.show();
                }
            }
        });

        createAnAccount.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                launchActivity(CreateAccountActivity.class);
            }
        });
    }

    private void launchActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        finish();
    }

    private boolean checkData(){
        //Danila's function

        if(email.getText().toString().equals("admin")
                && password.getText().toString().equals("admin")){

            return pushButton = true;
        }
        return pushButton;
    }
}
