package com.example.polyfinderv2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class NewRequestActivity extends AppCompatActivity {

    private final int GALLERY_REQUEST = 20;
    private final int REQUEST_CAMERA = 2;
    //private Button publish;
    private EditText title;
    private EditText description;
    private ToggleButton toggle_button;
    private Bitmap newBitmap = null;
    private ImageButton itemImage;
    private ScrollView scrollView;
    private boolean switchButton = false;
    private Spinner spinner;
    private Point size;
    private BitmapHelper bitmapHelper;
    private List<String> arrayOfOptions = new ArrayList<>();
    private Toolbar toolbar;
    private Picasso picasso;

    private DatabaseReference requestDatabase;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private String current_user_id;

    private static final int GALLERY_PICK = 1;
    private ProgressDialog image_load_progress;
    private String request_id;
    private String request_image_url = "default";
    private String request_thumb_image_url = "default";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_request_activity);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle("Create New Request");

        getDisplayMetric();
        getAllViews();
        changeColorListener();
        setOptionsForSpinner();
        setAdapterSpinner();
        setClickListener();

        requestDatabase = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        current_user_id = auth.getCurrentUser().getUid();
    }

    private void getAllViews() {
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        toggle_button = findViewById(R.id.choice_button);
        scrollView = findViewById(R.id.scrollView);
        itemImage = findViewById(R.id.photoImage);
        spinner = findViewById(R.id.spinner);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate((R.menu.new_request_menu),menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.publish_button:
                publishToScrollView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getDisplayMetric() {
        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
    }

    private void setOptionsForSpinner() {
        arrayOfOptions.add("Electronics");
        arrayOfOptions.add("Clothing");
        arrayOfOptions.add("Eat");
        arrayOfOptions.add("Documents");
        arrayOfOptions.add("Others");
    }

    private void setAdapterSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrayOfOptions);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setPrompt("Set category");
        spinner.setSelection(3);
    }

    private void changeColorListener() {
        toggle_button.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (switchButton) {
                    toggle_button.setBackgroundColor(getColor(R.color.foundColor));
                    toggle_button.setText("Found");
                    toggle_button.setTextColor(Color.WHITE);
                    switchButton = false;
                } else {
                    toggle_button.setBackgroundColor(getColor(R.color.lostColor));
                    toggle_button.setText("Lost");
                    toggle_button.setTextColor(Color.WHITE);
                    switchButton = true;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        returnToMainActivity();
    }

    private void setClickListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        itemImage.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                setPhotoFromPhone();
            }
        });
    }

    private void setPhotoFromPhone() {
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
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                image_load_progress = new ProgressDialog(NewRequestActivity.this);
                image_load_progress.setTitle("Загружаем изображение");
                image_load_progress.setMessage("Подождите, пока мы загружаем вашу фотографию");
                image_load_progress.setCanceledOnTouchOutside(false);
                image_load_progress.show();

                Uri resultUri = result.getUri();

                File thumb_file = new File(resultUri.getPath());

                Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxHeight(100)
                        .setMaxWidth(100)
                        .compressToBitmap(thumb_file);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                DatabaseReference user_message_push = requestDatabase.child("requests").push();


                request_id = user_message_push.getKey();

                final StorageReference filepath = storageReference.child("Request_Images").child(request_id + ".jpg");
                final StorageReference thumb_filepath = storageReference.child("Request_Images").child("thumbs").child(request_id + ".jpg");
                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                final String download_link = uri.toString();
                                request_image_url = download_link;

                                UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        thumb_filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String thumb_download_url = uri.toString();
                                                request_thumb_image_url = thumb_download_url;

                                                image_load_progress.dismiss();
                                                Toast.makeText(NewRequestActivity.this, "Successfully Uploaded!", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                });

                            }
                        });
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(NewRequestActivity.this, (CharSequence) error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void returnToMainActivity() {
        finish();
        overridePendingTransition(0, 0);
    }

    private void publishToScrollView() {
        final Intent intent = new Intent();

        String type = "";

        if(switchButton){
            type = "lost";
        }else{
            type = "found";
        }

        String title_txt = title.getText().toString();
        String spinner_txt = spinner.getSelectedItem().toString();
        String description_txt = description.getText().toString();

        if(TextUtils.isEmpty(title_txt)||TextUtils.isEmpty(spinner_txt)||TextUtils.isEmpty(description_txt)) {
            Toast.makeText(NewRequestActivity.this, "Заполните Все Поля!", Toast.LENGTH_SHORT).show();
        } else {

            Map requestMap = new HashMap();
            requestMap.put("title", title_txt);
            requestMap.put("category", spinner_txt);
            requestMap.put("description", description_txt);
            requestMap.put("time", ServerValue.TIMESTAMP);
            requestMap.put("from", current_user_id);
            requestMap.put("type", type);
            requestMap.put("image", request_image_url);
            requestMap.put("thumb_image", request_thumb_image_url);

            requestDatabase.child("Requests").child(request_id).setValue(requestMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        intent.putExtra("title",title.getText().toString());
                        intent.putExtra("category",spinner.getSelectedItem().toString());
                        intent.putExtra("description",description.getText().toString());
                        intent.putExtra("fragment",switchButton);
                        intent.putExtra("image", request_image_url);
                        intent.putExtra("thumb_image", request_thumb_image_url);

                        returnToMainActivity();
                    }
                }
            });
        }
    }
}