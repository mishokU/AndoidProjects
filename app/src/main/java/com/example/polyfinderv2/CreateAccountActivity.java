package com.example.polyfinderv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class CreateAccountActivity extends AppCompatActivity {

    private Button createAcc;



    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.create_account_activity);

        findAllViews();
        setActions();
    }

    private void findAllViews() {
        createAcc = findViewById(R.id.create);
    }

    private void setActions(){
        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMainActivity();
            }
        });
    }

    private void launchMainActivity() {
        Intent mainActivity = new Intent( this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }
}
