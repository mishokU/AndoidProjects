package com.example.polyfinderv2;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SignInActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private TextView register_here;
    private List<View> arrayOfEditText = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.sign_in_activity);

        findAllViews();
        setOnAction();
        setFocusListener();
    }

    private void setFocusListener() {
        for(int i = 0; i < arrayOfEditText.size(); i++) {
            final int index = i;
            arrayOfEditText.get(index).setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        arrayOfEditText.get(index).getBackground().setColorFilter(getColor(R.color.iconColor), PorterDuff.Mode.SRC_ATOP);
                    } else {
                        arrayOfEditText.get(index).getBackground().setColorFilter(getColor(R.color.borderColor), PorterDuff.Mode.SRC_ATOP);
                    }
                }
            });
        }
    }

    private void findAllViews() {
        arrayOfEditText.add(email = findViewById(R.id.email));
        arrayOfEditText.add(password = findViewById(R.id.password_text));
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
