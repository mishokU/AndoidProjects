package com.example.polyfinderv2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

public class OpenRequest extends AppCompatActivity {

    private TextView titleView;
    private TextView descriptionView;
    private TextView who;
    private TextView data;
    private TextView item;
    private TextView category;
    private ImageView imagePhoto;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.open_request);

        findAllViews();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setImageHeight();
        getDataFromTape();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToTape();
    }

    private void findAllViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        titleView = findViewById(R.id.title);
        descriptionView = findViewById(R.id.description);
        who = findViewById(R.id.who);
        data = findViewById(R.id.dataview);
        imagePhoto = findViewById(R.id.photoImage);
        category = findViewById(R.id.category);
    }

    private void setImageHeight(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        imagePhoto.getLayoutParams().height = size.x;
        imagePhoto.requestLayout();
    }

    private void getDataFromTape() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            category.setText(bundle.getString("category"));
            who.setText(bundle.getString("who"));
            data.setText(bundle.getString("data"));

            titleView.setText(bundle.getString("title"));
            titleView.setEnabled(false);
            descriptionView.setText(bundle.getString("description"));
            descriptionView.setEnabled(false);

            if(bundle.getString("item").equals("Found Item")){
                getSupportActionBar().setTitle("Found Item");
            } else {
                getSupportActionBar().setTitle("Lost Item");
            }

            if(bundle.getByteArray("image") != null) {
                byte[] byteArray = bundle.getByteArray("image");
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                imagePhoto.setImageBitmap(bmp);
            }
        }
    }

    private void backToTape() {
        finish();
        overridePendingTransition(0, 0);
    }

}
