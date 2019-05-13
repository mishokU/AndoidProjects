package com.example.polyfinderv2;

import android.app.ProgressDialog;
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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

import static android.content.Intent.ACTION_PICK;
import static android.content.Intent.getIntentOld;

public class ProfileActivity extends AppCompatActivity {

    static final int GALLERY_REQUEST = 1;
    private  Toolbar toolbar;
    //Data from views
    private ImageButton setPhoto;
    private ImageView photoImage;
    private Button saveInfoButton;
    private EditText email;//МИША БЛО ПОЧЕМУ ЭТО ПОЛЕ БЫЛО НАЗВАНО name просто штоо
    private EditText name;
    private EditText telephone;

    //firebase utils
    DatabaseReference reference;
    FirebaseUser currentUser;
    StorageReference storageReference;

    private ProgressDialog image_load_progress;
    private ProgressDialog info_progress;


    private static final int GALLERY_PICK = 1;

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

        storageReference = FirebaseStorage.getInstance().getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String txt_name = dataSnapshot.child("username").getValue().toString();
                String txt_email = dataSnapshot.child("email").getValue().toString();
                String image = dataSnapshot.child("imageUrl").getValue().toString();
                if(dataSnapshot.hasChild("phone")){
                    String phone = dataSnapshot.child("phone").getValue().toString();
                    telephone.setText(phone);
                }

                name.setText(txt_name);
                email.setText(txt_email);
                Picasso.with(ProfileActivity.this).load(image).placeholder(R.mipmap.ic_launcher).into(photoImage);
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
        photoImage = findViewById(R.id.photoImage);
        email = findViewById(R.id.email_field);
        name = findViewById(R.id.name_surname_field);
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

        info_progress = new ProgressDialog(ProfileActivity.this);
        info_progress.setTitle("Сохраняем изменения...");
        info_progress.setMessage("Подождите, пока мы добавим все изменения в ваш аккаунт:)");
        info_progress.show();
        String email_txt = email.getText().toString();
        String name_txt = name.getText().toString();
        String phone_txt = telephone.getText().toString();
        if(TextUtils.isEmpty(email_txt)||TextUtils.isEmpty(name_txt)||(TextUtils.isEmpty(phone_txt)&&phone_txt.length() < 11))
        {
            Toast.makeText(ProfileActivity.this, "all fields are required", Toast.LENGTH_SHORT).show();
        }
        else{

            Map changeMap = new HashMap<>();
            changeMap.put("email", email_txt);
            changeMap.put("username", name_txt);
            changeMap.put("phone", phone_txt);

            reference.updateChildren(changeMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isComplete()){

                        info_progress.dismiss();

                    } else{
                        info_progress.hide();
                        Toast.makeText(ProfileActivity.this, "An Error Occured", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    private void setPhotoFromPhone() {
        /*Intent photoPickerIntent = new Intent(ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);*/

        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(gallery, "SELECT IMAGE"), GALLERY_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            System.out.println("ERROR DOWNLOADING IMAGE");

            Uri imageUri = data.getData();//READY TO CROP THE IMAGE

            CropImage.activity(imageUri)
                    .setAspectRatio(2,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                image_load_progress = new ProgressDialog(ProfileActivity.this);
                image_load_progress.setTitle("Загружаем изображение");
                image_load_progress.setMessage("Подождите, пока мы обновляем вашу профильную фотографию");
                image_load_progress.setCanceledOnTouchOutside(false);
                image_load_progress.show();

                Uri resultUri = result.getUri();

                File thumb_file = new File(resultUri.getPath());

                String currentUid = currentUser.getUid();

                Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxHeight(200)
                        .setMaxWidth(100)
                        .compressToBitmap(thumb_file);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                final StorageReference filepath = storageReference.child("Profile_Images").child(currentUid + ".jpg");
                final StorageReference thumb_filepath = storageReference.child("Profile_Images").child("thumbs").child(currentUid + ".jpg");
                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                final String download_link = uri.toString();

                                Map updateMap = new HashMap<>();
                                updateMap.put("imageUrl", download_link);


                                reference.updateChildren(updateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            image_load_progress.dismiss();
                                            Toast.makeText(ProfileActivity.this, "Successfully Uploaded!", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                            }
                        });
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(ProfileActivity.this, (CharSequence) error, Toast.LENGTH_SHORT).show();
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
