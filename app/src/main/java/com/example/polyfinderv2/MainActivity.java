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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        LostFragment.LostFragmentListener,
        FoundFragment.FoundFragmentListener {

    private ImageButton goToProfileButton;
    private ImageButton requestButton;
    private ImageButton searchView;
    private android.support.v7.widget.Toolbar toolbar;
    private ImageButton backButton;
    private EditText searchText;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton sortView;
    private FoundFragment foundFragment;
    private LostFragment lostFragment;
    private ArrayList<View> bunchOfViews = new ArrayList<>();
    private int foundScrollDown = 0;
    private int lostScrollDown = 0;
    private int ToolBarPlusTabLayoutHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAllViews();

        setViewPagerAdapter();

        setOnClickListeners();

        addElementToScrollView();
    }

    private void setAllViews() {
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        bunchOfViews.add(searchView = findViewById(R.id.searchView));
        bunchOfViews.add(backButton = findViewById(R.id.backButton));
        bunchOfViews.add(searchText = findViewById(R.id.searchText));
        bunchOfViews.add(sortView = findViewById(R.id.sortButton));
        goToProfileButton = toolbar.findViewById(R.id.profile_button);
        requestButton = findViewById(R.id.request_button);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);
        //reloadData = findViewById(R.id.loadData);

        ToolBarPlusTabLayoutHeight = toolbar.getHeight() + tabLayout.getHeight() + 2;

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

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if(position == 0) {
                    if(foundScrollDown < toolbar.getHeight() + tabLayout.getHeight() + 2){
                        for (View b : bunchOfViews) {
                            b.setY(-foundScrollDown);
                        }
                        toolbar.setY(-foundScrollDown);
                        tabLayout.setY(-foundScrollDown + toolbar.getHeight());
                    } else {
                        for (View b : bunchOfViews) {
                            b.setY(-(toolbar.getHeight() + tabLayout.getHeight() + 2));
                        }
                        toolbar.setY(-(toolbar.getHeight() + tabLayout.getHeight() + 2));
                        tabLayout.setY(-(toolbar.getHeight() + tabLayout.getHeight() + 2) + toolbar.getHeight());
                    }
                }

                if(positionOffset >= 0.5) {
                    if(lostScrollDown < toolbar.getHeight() + tabLayout.getHeight() + 2){
                        for (View b : bunchOfViews) {
                            b.setY(-lostScrollDown);
                        }
                        toolbar.setY(-lostScrollDown);
                        tabLayout.setY(-lostScrollDown + toolbar.getHeight());
                    } else {
                        for (View b : bunchOfViews) {
                            b.setY(-(toolbar.getHeight() + tabLayout.getHeight() + 2));
                        }
                        toolbar.setY(-(toolbar.getHeight() + tabLayout.getHeight() + 2));
                        tabLayout.setY(-(toolbar.getHeight() + tabLayout.getHeight() + 2) + toolbar.getHeight());
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void closeCustomSearchView() {
        toolbar.setVisibility(View.VISIBLE);
        goToProfileButton.setVisibility(View.VISIBLE);

        backButton.setVisibility(View.INVISIBLE);
        searchText.setVisibility(View.INVISIBLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //searchView.setBackgroundColor(getColor(R.color.colorPrimary));
            backButton.setBackgroundColor(getColor(R.color.colorPrimary));
        }
        UIUtil.hideKeyboard(this);
    }

    private void openCustomSearchView() {
        toolbar.setVisibility(View.INVISIBLE);
        goToProfileButton.setVisibility(View.INVISIBLE);

        backButton.setVisibility(View.VISIBLE);
        backButton.setBackgroundColor(Color.WHITE);
        searchText.setVisibility(View.VISIBLE);
        UIUtil.showKeyboard(this, searchText);
        //searchView.setAnimation();
    }

    private void setViewPagerAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        foundFragment = new FoundFragment();
        lostFragment = new LostFragment();

        adapter.AddFragment(foundFragment, "Found");
        adapter.AddFragment(lostFragment, "Lost");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void addElementToScrollView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (!bundle.getBoolean("fragment")) {
                foundFragment.setArguments(bundle);
            } else {
                lostFragment.setArguments(bundle);
            }
        }
    }

    private void launch(Class classActivity) {
        Intent activity = new Intent(this, classActivity);

        if(classActivity == ProfileActivity.class){
            startActivity(activity);
        } else {
            startActivity(activity);
            finish();
        }
        overridePendingTransition(0, 0);
    }

    public void onRollUpping(Boolean roll,int yPosition) {
        if (yPosition <= toolbar.getHeight() + tabLayout.getHeight() + 2) {
            for (View b : bunchOfViews) {
                b.setY(-yPosition);
            }
            toolbar.setY(-yPosition);
            tabLayout.setY(-yPosition + toolbar.getHeight());
        } else {
            for (View b : bunchOfViews) {
                b.setY(-(toolbar.getHeight() + tabLayout.getHeight() + 2));
            }
            toolbar.setY(-(toolbar.getHeight() + tabLayout.getHeight() + 2));
            tabLayout.setY(-(toolbar.getHeight() + tabLayout.getHeight() + 2) + toolbar.getHeight());
        }
    }

    @Override
    public void onLostInputSent(Boolean rollUp, int yPos) {
        lostScrollDown = yPos;
        onRollUpping(rollUp, lostScrollDown);
    }

    @Override
    public void onFoundInputSent(Boolean rollUp, int yPos){
        foundScrollDown = yPos;
        onRollUpping(rollUp, foundScrollDown);
    }

    /*@Override
    public void onNewActivityGet(String title, String description, Boolean switchButton, ImageButton imageButton){

    }*/
}
