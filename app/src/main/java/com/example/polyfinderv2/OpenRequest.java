package com.example.polyfinderv2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class OpenRequest extends AppCompatActivity {

    ImageButton backbutton;
    TextView titleView;
    TextView descriptionView;
    ImageView imageView;
    TextView who;
    TextView data;
    TextView item;
    TextView category;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.open_request);

        findAllViews();
        setOnActions();
        getDataFromTape();
    }

    private void findAllViews() {
        backbutton = findViewById(R.id.backButton);
        titleView = findViewById(R.id.title);
        descriptionView = findViewById(R.id.description);
        imageView = findViewById(R.id.imageView);
        who = findViewById(R.id.who);
        data = findViewById(R.id.dataview);
        item = findViewById(R.id.item);
        category = findViewById(R.id.category);
    }

    private void getDataFromTape() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            item.setText(bundle.getString("item"));
            category.setText(bundle.getString("category"));
            titleView.setText(bundle.getString("title"));
            descriptionView.setText(bundle.getString("description"));
            who.setText(bundle.getString("who"));
            data.setText(bundle.getString("data"));
            //imageView.setImageBitmap(bundle.getString("image"));
        }
    }

    private void setOnActions() {
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToTape();
            }
        });
    }

    private void backToTape() {
        finish();
        overridePendingTransition(0, 0);
    }

}
