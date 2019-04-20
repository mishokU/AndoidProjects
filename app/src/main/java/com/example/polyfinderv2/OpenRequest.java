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

public class OpenRequest extends AppCompatActivity {

    private ImageButton backbutton;
    private TextView titleView;
    private TextView descriptionView;
    private ImageView imageView;
    private TextView who;
    private TextView data;
    private TextView item;
    private TextView category;
    private ImageView imagePhoto;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.open_request);

        findAllViews();
        setImageHeight();
        setOnActions();
        getDataFromTape();
    }

    private void findAllViews() {
        backbutton = findViewById(R.id.backButton);
        titleView = findViewById(R.id.title);
        descriptionView = findViewById(R.id.description);
        who = findViewById(R.id.who);
        data = findViewById(R.id.dataview);
        imagePhoto = findViewById(R.id.photoImage);
        item = findViewById(R.id.item);
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
            item.setText(bundle.getString("item"));
            category.setText(bundle.getString("category"));
            who.setText(bundle.getString("who"));
            data.setText(bundle.getString("data"));

            titleView.setText(bundle.getString("title"));
            titleView.setEnabled(false);
            descriptionView.setText(bundle.getString("description"));
            descriptionView.setEnabled(false);

            if(bundle.getByteArray("image") != null) {
                byte[] byteArray = bundle.getByteArray("image");
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                imagePhoto.setImageBitmap(bmp);
            }
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
