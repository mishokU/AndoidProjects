package com.example.polyfinderv2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FoundFragment extends Fragment implements Filterable {

    private NestedScrollView foundScrollView;
    private LinearLayout foundMainTape;
    private View foundView;
    private Bitmap bmp = null;
    private BitmapHelper bitmapHelper;
    private List<RectangleRequest> RectangleRequestList = new ArrayList<>();

    DatabaseReference requestDatabase;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        foundView = inflater.inflate(R.layout.found_fragment, container, false);

        findAllViews();

        requestDatabase = FirebaseDatabase.getInstance().getReference().child("Requests");

        loadRequests();

        return foundView;
    }

    private void findAllViews() {
        foundMainTape = foundView.findViewById(R.id.mainTape);
        foundScrollView = foundView.findViewById(R.id.nestedscrollview);
    }

    public void openFullRequest(final String image, final String from) {
        for (int i = 0; i < foundMainTape.getChildCount(); i++) {
            final int index = i;
            foundMainTape.getChildAt(i).findViewById(R.id.constraintlayout).setOnClickListener(new View.OnClickListener() {
                TextView titleText = foundMainTape.getChildAt(index).findViewById(R.id.title);
                TextView description = foundMainTape.getChildAt(index).findViewById(R.id.description);
                //TextView whoFind = foundMainTape.getChildAt(index).findViewById(R.id.person_name);
                TextView data = foundMainTape.getChildAt(index).findViewById(R.id.dataview);
                TextView category = foundMainTape.getChildAt(index).findViewById(R.id.category);

                @Override
                public void onClick(View v) {
                    launchOpenRequestActivity(titleText.getText().toString(), description.getText().toString(), data.getText().toString(), category.getText().toString(), image, from);
                }
            });
        }
    }

    public void addNewElementToScrollView(Requests request, String type, String from) {
        if(request != null) {

            RectangleRequest rectangleRequest = new RectangleRequest(getContext());

            rectangleRequest.setCategoryView(request.getCategory());
            rectangleRequest.setTitle(request.getTitle());
            rectangleRequest.setDescription(request.getDescription());
            rectangleRequest.setImageView(request.getThumb_image(), type);

            //addViewToTheServer();

            foundMainTape.addView(rectangleRequest.getRectangleRequestView(), 0);
            RectangleRequestList.add(rectangleRequest);
            openFullRequest(request.getImage(), from);
        }
    }

    private void launchOpenRequestActivity(String title, String description, String data, String category, String image, String from) {
        Intent request = new Intent(getActivity(), OpenRequest.class);
        request.putExtra("category", category);
        request.putExtra("title", title);
        request.putExtra("item", "Found Item");
        request.putExtra("description", description);
        request.putExtra("from", from);
        request.putExtra("data", data);
        request.putExtra("image", image);
        startActivity(request);
    }

    private void loadRequests(){
        requestDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Requests request = dataSnapshot.getValue(Requests.class);
                String type = request.getType();
                String from = request.getFrom();
                if(type.equals("found")){
                    addNewElementToScrollView(request, type, from);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public Filter getFilter() {
        return textFilter;
    }

    private Filter textFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<RectangleRequest> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0 || constraint == "All"){
                filteredList.addAll(RectangleRequestList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(RectangleRequest rectangleRequest: RectangleRequestList){
                    if((rectangleRequest.getTitleText().toString().toLowerCase().contains(filterPattern)) ||
                       (rectangleRequest.getDescriptionText().toString().toLowerCase().contains(filterPattern)) ||
                       (rectangleRequest.getCategoryView().toString().toLowerCase().contains(filterPattern))){
                        filteredList.add(rectangleRequest);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<RectangleRequest> list = new ArrayList<RectangleRequest>((List) results.values);
            foundMainTape.removeAllViews();
            for(RectangleRequest rectangleRequest: list) {
                foundMainTape.addView(rectangleRequest.getRectangleRequestView());
            }
        }
    };
}
