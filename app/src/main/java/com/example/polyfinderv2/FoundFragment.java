package com.example.polyfinderv2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class FoundFragment extends Fragment {

    private FoundFragmentListener listener;
    private ScrollView foundScrollView;
    private LinearLayout foundMainTape;
    private View foundView;

    public interface FoundFragmentListener {
        void onFoundInputSent(Boolean rollUp, int yPos);
        //void onNewActivityGet()
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        foundView = inflater.inflate(R.layout.found_fragment, container, false);

        findAllViews();
        addNewElementToScrollView();
        setOnActions();
        openFullRequest();
        return foundView;
    }

    private void findAllViews() {
        foundScrollView = foundView.findViewById(R.id.foundscrollView);
        foundMainTape = foundScrollView.findViewById(R.id.foundmainTape);

    }

    private void openFullRequest() {
        if(foundMainTape.getChildCount() > 1) {
            for (int i = 1; i < foundMainTape.getChildCount(); i++) {
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
    }

    private void addNewElementToScrollView() {
        Bundle bundle = getArguments();

        if(bundle != null) {

            View view = getLayoutInflater().inflate(R.layout.request_rectangle, null);

            TextView title = view.findViewById(R.id.title);
            TextView description = view.findViewById(R.id.description);
            TextView dateView = view.findViewById(R.id.dataview);
            TextView categoryView = view.findViewById(R.id.category);

            categoryView.setText(bundle.getString("category"));

            String titleText = bundle.getString("title");
            String descText = bundle.getString("description");

            title.setText(titleText);
            title.setLines(1);
            title.setEnabled(false);

            description.setText(descText);
            description.setLines(2);
            description.setEnabled(false);

            Date date = new Date();
            dateView.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(date));
            dateView.setTextColor(Color.LTGRAY);
            dateView.setEnabled(false);

            //addViewToTheServer();

            foundMainTape.addView(view, 1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setOnActions() {
        foundScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener(){

            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY <= 70) {
                    listener.onFoundInputSent(false, scrollY);
                } else {
                    listener.onFoundInputSent(true, scrollY);
                }
            }
        });
    }

    private void launchOpenRequestActivity(String title, String description, String whoFind, String data, String category, ImageView image) {
        Intent request = new Intent(getActivity(), OpenRequest.class);
        request.putExtra("category", category);
        request.putExtra("title", title);
        request.putExtra("item", "Found Item");
        request.putExtra("description", description);
        request.putExtra("who", whoFind);
        request.putExtra("data", data);
        //request.putExtra("image", image);
        startActivity(request);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof FoundFragmentListener){
            listener = (FoundFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement LostFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
