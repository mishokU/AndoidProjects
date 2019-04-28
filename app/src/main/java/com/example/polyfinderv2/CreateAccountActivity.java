package com.example.polyfinderv2;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateAccountActivity extends AppCompatActivity {

    private Button createAcc;
    private ImageButton backToLogIn;
    private TextView sign_in;
    private EditText name_surname;
    private EditText email;
    private EditText password;
    private EditText re_enter_password;
    private List<View> arrayOfEditText = new ArrayList<View>();

    //FireBase authentication
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.create_account_activity);

        mAuth = FirebaseAuth.getInstance();

        findAllViews();
        setFocusListener();
        setActions();

        createAcc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String txt_login = name_surname.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_passwordApproval = re_enter_password.getText().toString();


                if(TextUtils.isEmpty(txt_login) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(CreateAccountActivity.this, "all fields are required", Toast.LENGTH_SHORT).show();
                } else if(!txt_password.equals(txt_passwordApproval)){
                    Toast.makeText(CreateAccountActivity.this, "passwords are not equal", Toast.LENGTH_SHORT).show();
                } else{
                    //progress.setTitle("Регистрируем Вас.");
                    //progress.setMessage("Подождите, пока мы создаем Вам аккаунт :)");
                    //progress.setCanceledOnTouchOutside(false);
                    //progress.show();
                    register(txt_email, txt_login, txt_password);
                }

            }
        });
    }


    private void register(String email, final String login, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String user_id = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("username", login);
                            hashMap.put("imageUrl", "default");


                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        //progress.dismiss();
                                        Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                        } else{
                            //progress.hide();
                            Toast.makeText(CreateAccountActivity.this, "You can't register with this email and password.", Toast.LENGTH_SHORT).show();
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
        createAcc = findViewById(R.id.create);
        sign_in = findViewById(R.id.sign_in);
        arrayOfEditText.add(name_surname = findViewById(R.id.name_surname_field));
        arrayOfEditText.add(email = findViewById(R.id.email_field));
        arrayOfEditText.add(password = findViewById(R.id.password_field));
        arrayOfEditText.add(re_enter_password = findViewById(R.id.reenter_password_field));
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
