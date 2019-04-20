package com.example.polyfinderv2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.ACTION_PICK;
import static com.example.polyfinderv2.ProfileActivity.GALLERY_REQUEST;
import static java.security.AccessController.getContext;

public class NewRequestActivity extends AppCompatActivity {

    private final int GALERY_REQUEST = 1;
    private Button publish;
    private EditText title;
    private EditText description;
    private Button lost_button;
    private ImageButton backButton;
    private Bitmap newBitmap = null;
    private ImageButton itemImage;
    private ScrollView scrollView;
    private boolean switchButton = false;
    private Spinner spinner;
    private List<String> arrayOfOptions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_request_activity);

        getAllViews();
        changeColorListener();
        setOptionsForSpinner();
        setAdapterSpinner();
        setClickListener();
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
        lost_button.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (switchButton) {
                    lost_button.setBackgroundColor(getColor(R.color.foundColor));
                    lost_button.setText("Found");
                    lost_button.setTextColor(Color.WHITE);
                    switchButton = false;
                } else {
                    lost_button.setBackgroundColor(getColor(R.color.lostColor));
                    lost_button.setText("Lost");
                    lost_button.setTextColor(Color.WHITE);
                    switchButton = true;
                }
            }
        });
    }

    private void setClickListener() {
        publish.setOnClickListener(new View.OnClickListener() {

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
        Intent photoPickerIntent = new Intent(ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap;

        float epsilonWidth;
        float epsilonHeight;

        switch (requestCode){
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    try {

                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);

                        epsilonWidth = bitmap.getWidth() / (float)itemImage.getWidth();
                        epsilonHeight = bitmap.getHeight() / (float)itemImage.getHeight();

                        float newWidth = bitmap.getWidth() / epsilonWidth;
                        float newHeight = bitmap.getHeight() / epsilonHeight;

                        newBitmap = Bitmap.createScaledBitmap(bitmap, (int)newWidth, (int)newHeight, false);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    itemImage.setImageBitmap(newBitmap);
                }
        }
    }

    private void getAllViews() {
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        lost_button = findViewById(R.id.lost_button);
        publish = findViewById(R.id.publish_button);
        backButton = findViewById(R.id.backButton);
        scrollView = findViewById(R.id.scrollView);
        itemImage = findViewById(R.id.photoImageButton);
        spinner = findViewById(R.id.spinner);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        itemImage.getLayoutParams().height = size.x;
        itemImage.requestLayout();
    }

    private void returnToMainActivity() {
        finish();
        overridePendingTransition(0, 0);
    }

    private void publishToScrollView() {
        Intent intent = new Intent();

        intent.putExtra("title",title.getText().toString());
        intent.putExtra("category",spinner.getSelectedItem().toString());
        intent.putExtra("description",description.getText().toString());
        intent.putExtra("fragment",switchButton);

        itemImage.invalidate();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) itemImage.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        if(bitmap != null) {
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
            byte[] byteArray = bStream.toByteArray();
            intent.putExtra("image", byteArray);
        }
        setResult(RESULT_OK, intent);
        returnToMainActivity();
    }

}
