package com.example.polyfinderv2;

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
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.ACTION_GET_CONTENT;

public class NewRequestActivity extends AppCompatActivity {

    private final int GALLERY_REQUEST = 20;
    private final int REQUEST_CAMERA = 2;
    //private Button publish;
    private EditText title;
    private EditText description;
    private Button lost_button;
    private ImageButton backButton;
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

    public static Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {
        // Читаем с inJustDecodeBounds=true для определения размеров
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Вычисляем inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Читаем с использованием inSampleSize коэффициента
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Реальные размеры изображения
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Вычисляем наибольший inSampleSize, который будет кратным двум
            // и оставит полученные размеры больше, чем требуемые
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private void setPhotoFromPhone() {
        final CharSequence[] items={"Camera","Gallery","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(NewRequestActivity.this);
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               if(items[which].equals("Camera")){
                   Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                   startActivityForResult(intent, REQUEST_CAMERA);

               } else if(items[which].equals("Gallery")) {
                   Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                   File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                   String string = file.getPath();
                   Uri uri = Uri.parse(string);
                   intent.setDataAndType(uri,"image/*");
                   startActivityForResult(intent, GALLERY_REQUEST);
               } else if(items[which].equals("Cancel")) {
                   dialog.dismiss();
               }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {

            if (requestCode == GALLERY_REQUEST) {
                Uri selectedImage = data.getData();
                Picasso.get().load(selectedImage).resize(size.x,size.x).into(itemImage);
                //System.out.print("Bitmap size" + );
            } else if(requestCode == REQUEST_CAMERA) {
                Uri selectedImage = data.getData();
                Picasso.get().load(selectedImage).resize(size.x,size.x).into(itemImage);

            }
        }
    }

    private void getAllViews() {
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        lost_button = findViewById(R.id.lost_button);
        scrollView = findViewById(R.id.scrollView);
        itemImage = findViewById(R.id.photoImage);
        spinner = findViewById(R.id.spinner);


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

        if(newBitmap != null) {
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            newBitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
            byte[] byteArray = bStream.toByteArray();
            intent.putExtra("image", byteArray);
        }

        setResult(RESULT_OK, intent);
        returnToMainActivity();
    }

}
