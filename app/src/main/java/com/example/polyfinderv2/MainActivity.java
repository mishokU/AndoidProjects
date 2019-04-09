package com.example.polyfinderv2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

public class MainActivity extends AppCompatActivity {

    private ImageButton goToProfileButton;
    private ImageButton requestButton;
    private ImageButton searchView;
    private android.support.v7.widget.Toolbar toolbar;
    private ImageButton backButton;
    private EditText searchText;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FoundFragment foundFragment;
    private LostFragment lostFragment;

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

        foundFragment = new FoundFragment();
        lostFragment = new LostFragment();

        adapter.AddFragment(foundFragment, "Found");
        adapter.AddFragment(lostFragment, "Lost");

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
        //reloadData = findViewById(R.id.loadData);

    }

    private void addElementToScrollView(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            if(!bundle.getBoolean("fragment")) {
                foundFragment.setArguments(bundle);
            } else {
                lostFragment.setArguments(bundle);
            }
        }
    }

    private void launch(Class classActivity){
        Intent activity = new Intent(this, classActivity);
        startActivity(activity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        finish();
    }
}
