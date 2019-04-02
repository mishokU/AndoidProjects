package com.example.polyfinderv2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {

    private ImageButton goToProfileButton;
    private ImageButton requestButton;
    private LinearLayout mainTape;
    private ScrollView scrollView;
    private EditText titleRequest;
    private EditText descriptionRequest;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        setAllViews();

        setOnClickListeners();

        addElementToScrollView();
    }

    private void setOnClickListeners() {
        goToProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(ProfileActivity.class);
            }
        });

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(NewRequestActivity.class);
            }
        });
    }

    private void setAllViews() {
        goToProfileButton = findViewById(R.id.profile_button);
        requestButton = findViewById(R.id.request_button);

        scrollView = findViewById(R.id.scrollView);
        mainTape = scrollView.findViewById(R.id.mainTape);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addElementToScrollView() {

        Intent requestTake = getIntent();
        Bundle requestBundle = requestTake.getExtras();

        if(requestBundle != null) {

            String title = (String)requestBundle.get("title");
            String description = (String)requestBundle.get("description");
            boolean color = (boolean)requestBundle.get("color");

            View request = getLayoutInflater().inflate(R.layout.request_rectangle, null);
            ImageView find = request.findViewById(R.id.imageView2);

            //Set background color to new request depend on witch color we get from user
            if(color){
                find.setColorFilter(getColor(R.color.lostColor));
            } else {
                find.setColorFilter(getColor(R.color.foundColor));
            }

            titleRequest = request.findViewById(R.id.title);
            titleRequest.setLines(1);
            titleRequest.setEnabled(false);

            descriptionRequest = request.findViewById(R.id.description);
            descriptionRequest.setEnabled(false);
            descriptionRequest.setLines(2);

            titleRequest.setText(title);
            descriptionRequest.setText(description);

            mainTape.addView(request,0);
        }
    }

    private void launch(Class classActivity){
        Intent activity = new Intent(this, classActivity);
        startActivity(activity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        finish();
    }
}
