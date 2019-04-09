package com.example.polyfinderv2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class NewRequestActivity extends AppCompatActivity {

    private Button publish;
    private EditText title;
    private EditText description;
    private Button lost_button;
    private ImageButton backButton;
    private ScrollView scrollView;
    private boolean switchButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_request_activity);

        getAllViews();
        setClickListener();
    }

    private void setClickListener() {
        publish.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                publishToScrollView();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMainActivity();
            }
        });

        lost_button.setOnClickListener(new View.OnClickListener(){

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(switchButton) {
                    lost_button.setBackgroundColor(getColor(R.color.foundColor));
                    lost_button.setText("Found");
                    lost_button.setTextColor(Color.WHITE);
                    switchButton = false;
                }else{
                    lost_button.setBackgroundColor(getColor(R.color.lostColor));
                    lost_button.setText("Lost");
                    lost_button.setTextColor(Color.WHITE);
                    switchButton = true;
                }
            }
        });
    }

    private void getAllViews(){
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        lost_button = findViewById(R.id.lost_button);
        publish = findViewById(R.id.publish_button);
        backButton = findViewById(R.id.backButton);
        scrollView = findViewById(R.id.scrollView);
    }

    private void returnToMainActivity() {
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }

    private void publishToScrollView() {
        //Add to data base and publish to main tape
        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra("title", title.getText().toString());
        intent.putExtra("description", description.getText().toString());
        intent.putExtra("fragment", switchButton);

        startActivity(intent);
        finish();
    }
}
