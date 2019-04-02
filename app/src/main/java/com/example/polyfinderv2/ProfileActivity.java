package com.example.polyfinderv2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.IOException;

import static android.content.Intent.ACTION_PICK;

public class ProfileActivity extends AppCompatActivity {

    static final int GALLERY_REQUEST = 1;

    private ImageButton backButton;

    //Data from views
    private ImageButton setPhoto;
    private Button saveInfoButton;
    private EditText name;
    private EditText surname;
    private EditText direction;
    private EditText telephone;
    private EditText group;
    private EditText vkLink;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        findAllViews();
        setActions();
    }

    private void findAllViews(){
        backButton = findViewById(R.id.backButton);
        setPhoto = findViewById(R.id.photoImageButton);
        name = findViewById(R.id.human_name);
        surname = findViewById(R.id.human_surname);
        direction = findViewById(R.id.direction);
        telephone = findViewById(R.id.telephone_field);
        group = findViewById(R.id.group_in_university);
        vkLink = findViewById(R.id.vk_link);
        saveInfoButton = findViewById(R.id.save_info_button);
    }

    private void setActions(){

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMainActivity();
            }
        });

        setPhoto.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                setPhotoFromPhone();
            }
        });

        saveInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInformationToServer();
            }
        });
    }

    private void saveInformationToServer() {
        //Danila's code

    }

    private void setPhotoFromPhone() {
        Intent photoPickerIntent = new Intent(ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap;
        Bitmap newBitmap = null;
        float epsilonWidth;
        float epsilonHeight;

        switch (requestCode){
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    try {

                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);

                        epsilonWidth = bitmap.getWidth() / (float)setPhoto.getWidth();
                        epsilonHeight = bitmap.getHeight() / (float)setPhoto.getHeight();

                        float newWidth = bitmap.getWidth() / epsilonWidth;
                        float newHeight = bitmap.getHeight() / epsilonHeight;

                        newBitmap = Bitmap.createScaledBitmap(bitmap, (int)newWidth, (int)newHeight, false);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),newBitmap);
                    roundedBitmapDrawable.setCircular(true);
                    setPhoto.setImageDrawable(roundedBitmapDrawable);
                }
        }
    }

    private void returnToMainActivity() {
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }
}
