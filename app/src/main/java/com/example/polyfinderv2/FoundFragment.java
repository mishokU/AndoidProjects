package com.example.polyfinderv2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FoundFragment extends Fragment implements Filterable {

    private NestedScrollView foundScrollView;
    private LinearLayout foundMainTape;
    private View foundView;
    private Bitmap bmp = null;
    private BitmapHelper bitmapHelper;
    private List<RectangleRequest> RectangleRequestList = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        foundView = inflater.inflate(R.layout.found_fragment, container, false);

        findAllViews();

        return foundView;
    }

    private void findAllViews() {
        foundMainTape = foundView.findViewById(R.id.mainTape);
        foundScrollView = foundView.findViewById(R.id.nestedscrollview);
    }

    public void openFullRequest() {
        for (int i = 0; i < foundMainTape.getChildCount(); i++) {
            final int index = i;
            foundMainTape.getChildAt(i).findViewById(R.id.constraintlayout).setOnClickListener(new View.OnClickListener() {
                TextView titleText = foundMainTape.getChildAt(index).findViewById(R.id.title);
                TextView description = foundMainTape.getChildAt(index).findViewById(R.id.description);
                TextView whoFind = foundMainTape.getChildAt(index).findViewById(R.id.person_name);
                TextView data = foundMainTape.getChildAt(index).findViewById(R.id.dataview);
                TextView category = foundMainTape.getChildAt(index).findViewById(R.id.category);
                ImageView imageButton = foundMainTape.getChildAt(index).findViewById(R.id.imageView);

                @Override
                public void onClick(View v) {
                    launchOpenRequestActivity(titleText.getText().toString(), description.getText().toString(),
                            whoFind.getText().toString(), data.getText().toString(), category.getText().toString(), imageButton);
                }
            });
        }
    }

    public void addNewElementToScrollView(Bundle bundle) {
        if(bundle != null) {

            RectangleRequest rectangleRequest = new RectangleRequest(getContext());

            if(bundle.getByteArray("image") != null) {
                bitmapHelper = new BitmapHelper();
                bitmapHelper.createBitmap(bundle.getByteArray("image"));
                bitmapHelper.createResizedBitmap(100,100);
                rectangleRequest.setImageView(bitmapHelper.getResizedBitmap());
            }

            rectangleRequest.setCategoryView(bundle.getString("category"));
            rectangleRequest.setTitle(bundle.getString("title"));
            rectangleRequest.setDescription(bundle.getString("description"));

            //addViewToTheServer();

            foundMainTape.addView(rectangleRequest.getRectangleRequestView(), 0);
            RectangleRequestList.add(rectangleRequest);
            openFullRequest();
        }
    }

    private void launchOpenRequestActivity(String title, String description, String whoFind, String data, String category, ImageView image) {
        Intent request = new Intent(getActivity(), OpenRequest.class);
        request.putExtra("category", category);
        request.putExtra("title", title);
        request.putExtra("item", "Found Item");
        request.putExtra("description", description);
        request.putExtra("who", whoFind);
        request.putExtra("data", data);
        if(bitmapHelper != null) {
            request.putExtra("image", bitmapHelper.getByteArray(bitmapHelper.getOriginalBitmap()));
        }
        startActivity(request);
    }

    @Override
    public Filter getFilter() {
        return textFilter;
    }

    private Filter textFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<RectangleRequest> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(RectangleRequestList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(RectangleRequest rectangleRequest: RectangleRequestList){
                    if((rectangleRequest.getTitleText().toString().toLowerCase().contains(filterPattern)) ||
                       (rectangleRequest.getDescriptionText().toString().toLowerCase().contains(filterPattern))){
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
