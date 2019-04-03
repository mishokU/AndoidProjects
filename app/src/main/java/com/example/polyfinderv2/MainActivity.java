package com.example.polyfinderv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

public class MainActivity extends AppCompatActivity {

    private ImageButton goToProfileButton;
    private ImageButton requestButton;
    private LinearLayout mainTape;
    private ScrollView scrollView;
    private EditText titleRequest;
    private EditText descriptionRequest;
    private ImageButton searchView;
    private android.support.v7.widget.Toolbar toolbar;
    private ImageButton backButton;
    private EditText searchText;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAllViews();

        setOnClickListeners();

        setViewPagerAdapter();

        addElementToScrollView();
    }

    private void setOnClickListeners() {
        goToProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(ProfileActivity.class);
            }
        });

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(NewRequestActivity.class);
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomSearchView();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeCustomSearchView();
            }
        });
    }

    private void closeCustomSearchView() {
        toolbar.setVisibility(View.VISIBLE);
        goToProfileButton.setVisibility(View.VISIBLE);

        backButton.setVisibility(View.INVISIBLE);
        searchText.setVisibility(View.INVISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            searchView.setBackgroundColor(getColor(R.color.colorPrimary));
            backButton.setBackgroundColor(getColor(R.color.colorPrimary));
        }
        UIUtil.hideKeyboard(this);
    }

    private void openCustomSearchView() {
        toolbar.setVisibility(View.INVISIBLE);
        goToProfileButton.setVisibility(View.INVISIBLE);
        searchView.setBackgroundColor(Color.WHITE);

        backButton.setVisibility(View.VISIBLE);
        backButton.setBackgroundColor(Color.WHITE);
        searchText.setVisibility(View.VISIBLE);
        UIUtil.showKeyboard(this,searchText);
        //searchView.setAnimation();
    }

    private void setViewPagerAdapter(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new FoundFragment(), "Found");
        adapter.AddFragment(new LostFragment(), "Lost");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setAllViews() {
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        goToProfileButton = findViewById(R.id.profile_button);
        requestButton = findViewById(R.id.request_button);
        searchView = findViewById(R.id.searchView);
        backButton = findViewById(R.id.backButton);
        searchText = findViewById(R.id.searchText);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);

        scrollView = findViewById(R.id.scrollView);
        mainTape = scrollView.findViewById(R.id.mainTape);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addElementToScrollView() {

        Intent requestTake = getIntent();
        Bundle requestBundle = requestTake.getExtras();

        if(requestBundle != null) {

            String title = (String)requestBundle.get("title");
            String description = (String)requestBundle.get("description");
            boolean color = (boolean)requestBundle.get("color");

            View request = getLayoutInflater().inflate(R.layout.request_rectangle, null);

            //Set background color to new request depend on witch color we get from user

            titleRequest = request.findViewById(R.id.title);
            titleRequest.setLines(1);
            titleRequest.setEnabled(false);

            descriptionRequest = request.findViewById(R.id.description);
            descriptionRequest.setEnabled(false);
            descriptionRequest.setLines(2);

            titleRequest.setText(title);
            descriptionRequest.setText(description);

            mainTape.addView(request,0);
        }
    }

    private void launch(Class classActivity){
        Intent activity = new Intent(this, classActivity);
        startActivity(activity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        finish();
    }
}
