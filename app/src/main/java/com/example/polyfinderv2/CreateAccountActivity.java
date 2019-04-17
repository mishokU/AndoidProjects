package com.example.polyfinderv2;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CreateAccountActivity extends AppCompatActivity {

    private Button createAcc;
    private ImageButton backToLogIn;
    private TextView sign_in;
    private EditText name_surname;
    private EditText email;
    private EditText password;
    private EditText phone;
    private List<View> arrayOfEditText = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.create_account_activity);

        findAllViews();
        setFocusListener();
        setActions();
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
        backToLogIn = findViewById(R.id.backButton);
        createAcc = findViewById(R.id.create);
        sign_in = findViewById(R.id.sign_in);
        arrayOfEditText.add(name_surname = findViewById(R.id.name_surname_field));
        arrayOfEditText.add(email = findViewById(R.id.email_field));
        arrayOfEditText.add(password = findViewById(R.id.password_field));
        arrayOfEditText.add(phone = findViewById(R.id.telephone_field));
    }

    private void setActions(){
        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(MainActivity.class);
            }
        });
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(SignInActivity.class);
            }
        });
    }

    private void launchActivity(Class activity) {
        Intent intent = new Intent( this, activity);
        startActivity(intent);
        finish();
        overridePendingTransition(0,0);
    }
}
