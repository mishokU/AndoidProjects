package com.example.polyfinderv2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import static android.content.Intent.ACTION_PICK;
import static android.content.Intent.getIntentOld;

public class ProfileActivity extends AppCompatActivity {

    static final int GALLERY_REQUEST = 1;
    private  Toolbar toolbar;
    //Data from views
    private ImageView setPhoto;
    private Button saveInfoButton;
    private EditText email;//МИША БЛО ПОЧЕМУ ЭТО ПОЛЕ БЫЛО НАЗВАНО name просто штоо
    private EditText name;
    private EditText telephone;
    private EditText password;
    private EditText group;
    private EditText vkLink;

    //firebase utils
    DatabaseReference reference;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Profile");

        findAllViews();
        setActions();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        String user_id = currentUser.getUid();

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String txt_name = dataSnapshot.child("username").getValue().toString();
                String txt_email = dataSnapshot.child("email").getValue().toString();
                //String image = dataSnapshot.child("image").getValue().toString();
                //String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                name.setText(txt_name);
                email.setText(txt_email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        launchActivity(MainActivity.class);
    }

    private void findAllViews(){
        setPhoto = findViewById(R.id.photoImageButton);
        email = findViewById(R.id.email_field);
        name = findViewById(R.id.name_surname_field);
        password = findViewById(R.id.password_field);
        telephone = findViewById(R.id.telephone_field);
        saveInfoButton = findViewById(R.id.save_info_button);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate((R.menu.profile_menu),menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.exit_button:{
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;}
            case R.id.home:
                launchActivity(MainActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setActions(){
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

    private void launchActivity(Class activity) {
        if(activity == StartActivity.class) {
            Intent intent = new Intent(this, activity);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            finish();
        }
        overridePendingTransition(0,0);
    }
}
