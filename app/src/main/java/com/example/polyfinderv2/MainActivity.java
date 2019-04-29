package com.example.polyfinderv2;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private final int SECOND_ACTIVITY_REQUEST_CODE = 1;
    private FloatingActionButton requestButton;
    private android.support.v7.widget.Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FoundFragment foundFragment;
    private LostFragment lostFragment;
    private ViewPagerAdapter adapter;

    //Sort choice
    private String choice;

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

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(viewPager.getCurrentItem() == 1) {
                    lostFragment.getFilter().filter(s);
                } else {
                    foundFragment.getFilter().filter(s);
                }
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.app_bar_search:
                return true;
            case R.id.sort_button:
                openBuilderMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openBuilderMenu() {
        final CharSequence[] items={"Electronics","Clothing","Eat","Documents","Others","All"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setTitle("Sort by: ");

        builder.setSingleChoiceItems(items,1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choice = items[which].toString();
            }
        });
        builder.setPositiveButton("SHOW", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sortMainTape();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void sortMainTape() {
        if(viewPager.getCurrentItem() == 1) {
            lostFragment.getFilter().filter(choice);
        } else {
            foundFragment.getFilter().filter(choice);
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
