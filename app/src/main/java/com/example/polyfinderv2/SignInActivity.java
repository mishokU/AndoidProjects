package com.example.polyfinderv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {

    private TextView register_here;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.sign_in_activity);

        findAllViews();
        setOnAction();
    }

    private void findAllViews() {
        register_here = findViewById(R.id.register_here);
    }

    private void setOnAction() {
        register_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(CreateAccountActivity.class);
            }
        });
    }

    private void launchActivity(Class createAccountActivityClass) {
        Intent activity = new Intent(this, createAccountActivityClass);
        startActivity(activity);
        finish();
        overridePendingTransition(0,0);
    }

}
