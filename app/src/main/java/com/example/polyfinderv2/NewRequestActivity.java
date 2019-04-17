package com.example.polyfinderv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
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

import java.util.ArrayList;
import java.util.List;

public class NewRequestActivity extends AppCompatActivity {

    private Button publish;
    private EditText title;
    private EditText description;
    private Button lost_button;
    private ImageButton backButton;
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
        setSpinner();
        setClickListener();
    }

    private void setOptionsForSpinner() {
        arrayOfOptions.add("Electronics");
        arrayOfOptions.add("Clothing");
        arrayOfOptions.add("Eat");
        arrayOfOptions.add("Documents");
        arrayOfOptions.add("Others");
    }

    private void setSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrayOfOptions);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setPrompt("Set category");
        spinner.setSelection(3);
    }

    private void changeColorListener() {
        lost_button.setOnClickListener(new View.OnClickListener(){

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if(switchButton) {
                    lost_button.setBackgroundColor(getColor(R.color.foundColor));
                    lost_button.setText("Found");
                    lost_button.setTextColor(Color.WHITE);
                    switchButton = false;
                }else{
                    lost_button.setBackgroundColor(getColor(R.color.lostColor));
                    lost_button.setText("Lost");
                    lost_button.setTextColor(Color.WHITE);
                    switchButton = true;
                }
            }
        });
    }

    private void setClickListener() {
        publish.setOnClickListener(new View.OnClickListener(){

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
    }

    private void getAllViews(){
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        lost_button = findViewById(R.id.lost_button);
        publish = findViewById(R.id.publish_button);
        backButton = findViewById(R.id.backButton);
        scrollView = findViewById(R.id.scrollView);
        spinner = findViewById(R.id.spinner);
    }

    private void returnToMainActivity() {
        Intent mainActivity = new Intent(this, MainActivity.class);
        startActivity(mainActivity);
        finish();
        overridePendingTransition(0,0);
    }

    private void publishToScrollView() {
        //Add to data base and publish to main tape
        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra("title", title.getText().toString());
        intent.putExtra("category", spinner.getSelectedItem().toString());
        intent.putExtra("description", description.getText().toString());
        intent.putExtra("fragment", switchButton);

        startActivity(intent);
        finish();

        overridePendingTransition(0,0);
    }
}
