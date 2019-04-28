package com.example.polyfinderv2;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class SignInActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private TextView register_here;
    private Button sign_in_button;
    private List<View> arrayOfEditText = new ArrayList<View>();

    //FIREBASE UTILS
    private FirebaseAuth mAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.sign_in_activity);

        mAuth = FirebaseAuth.getInstance();

        findAllViews();
        setOnAction();
        setFocusListener();

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_login = email.getText().toString();
                String txt_password = password.getText().toString();
                if(TextUtils.isEmpty(txt_login)|| TextUtils.isEmpty(txt_password)){
                    Toast.makeText(SignInActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else{
                    //progress.setTitle("Авторизация...");
                    //progress.setMessage("Подождите, пока мы входим в Ваш аккаунт :)");
                    //progress.setCanceledOnTouchOutside(false);
                    //progress.show();
                    signIn(txt_login, txt_password);
                }
            }
        });
    }

    private void signIn(String login, String password){
        mAuth.signInWithEmailAndPassword(login, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //progress.dismiss();
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            finish();
                            //finish(StartActivity);
                        } else {
                            //progress.hide();
                            Toast.makeText(SignInActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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
        sign_in_button = findViewById(R.id.sign_in_button);

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
