package com.example.polyfinderv2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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
            DatabaseReference from = FirebaseDatabase.getInstance().getReference().child("Users").child(bundle.getString("from"));
            from.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("username").getValue().toString();
                    who.setText(name);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            data.setText(bundle.getString("data"));

            titleView.setText(bundle.getString("title"));
            titleView.setEnabled(false);
            descriptionView.setText(bundle.getString("description"));
            descriptionView.setEnabled(false);

            if(bundle.getString("item").equals("Found Item")){
                getSupportActionBar().setTitle("Found Item");
                //Picasso.with(OpenRequest.this).load(bundle.getString("image")).placeholder(R.mipmap.found).into(imagePhoto);
            } else {
                getSupportActionBar().setTitle("Lost Item");
                //Picasso.with(OpenRequest.this).load(bundle.getString("image")).placeholder(R.mipmap.lost).into(imagePhoto);
            }
        }
    }

    private void backToTape() {
        finish();
        overridePendingTransition(0, 0);
    }

}
