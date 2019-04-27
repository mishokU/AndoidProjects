package com.example.polyfinderv2;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private final int SECOND_ACTIVITY_REQUEST_CODE = 1;
    private FloatingActionButton requestButton;
    private ImageButton searchView;
    private android.support.v7.widget.Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageButton sortView;
    private FoundFragment foundFragment;
    private LostFragment lostFragment;
    private ViewPagerAdapter adapter;

    //FireBase Utils
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);


        setFirebaseAuth();

        setAllViews();

        setViewPagerAdapter();

        setOnClickListeners();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        if( currentUser == null){
            sendToStart();
        }
    }

    //CHECK IF USER IS AUTH
    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }


    private void setFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void setAllViews() {
        requestButton = findViewById(R.id.fab);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.profile_image);
        setSupportActionBar(toolbar);

    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
    }

    private void setOnClickListeners() {
        requestButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            launch(NewRequestActivity.class);
            }
        });

       toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(ProfileActivity.class);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate((R.menu.main_menu),menu);

        final MenuItem SearchItem = menu.findItem(R.id.app_bar_search);
        final android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) SearchItem.getActionView();

        searchView.setOnSearchClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setItemsVisibility(menu,SearchItem, false);
            }
        });

        searchView.setOnCloseListener(new android.support.v7.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setItemsVisibility(menu, SearchItem, true);
                return false;
            }
        });

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }

    private void setItemsVisibility(Menu menu, MenuItem exception, boolean visible) {
        for (int i=0; i<menu.size(); ++i) {
            MenuItem item = menu.getItem(i);
            if (item != exception) item.setVisible(visible);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.app_bar_search:
                Toast.makeText(this,"Search", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sort_button:
                Toast.makeText(this,"Sort", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setViewPagerAdapter() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        foundFragment = new FoundFragment();
        lostFragment = new LostFragment();

        adapter.AddFragment(foundFragment, "Found");
        adapter.AddFragment(lostFragment, "Lost");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void launch(Class classActivity) {
        Intent activity = new Intent(this, classActivity);

        if(classActivity == NewRequestActivity.class){
            startActivityForResult(activity, SECOND_ACTIVITY_REQUEST_CODE);
        } else {
            startActivity(activity);
        }
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    if (!bundle.getBoolean("fragment")) {
                        foundFragment.addNewElementToScrollView(bundle);
                    } else {
                        lostFragment.addNewElementToScrollView(bundle);
                    }
                }
            }
        }
    }
}
