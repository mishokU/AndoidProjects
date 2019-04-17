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

public class LostFragment extends Fragment {

    private LostFragmentListener listener;
    private View lostView;
    private ScrollView lostScrollView;
    private LinearLayout lostMainTape;

    public interface LostFragmentListener {
        void onLostInputSent(Boolean rollUp,int yPos);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        lostView = inflater.inflate(R.layout.lost_fragment, container, false);

        findAllView();
        addNewElementToScrollView();
        setOnActions();
        openFullRequest();

        return lostView;
    }

    private void findAllView() {
        lostScrollView = lostView.findViewById(R.id.lostscrollView);
        lostMainTape = lostScrollView.findViewById(R.id.lostmainTape);
    }

    private void addNewElementToScrollView() {
        Bundle bundle = getArguments();
        if(bundle != null) {

            View view = getLayoutInflater().inflate(R.layout.request_rectangle, null);

            TextView title = view.findViewById(R.id.title);
            TextView description = view.findViewById(R.id.description);
            TextView dateView = view.findViewById(R.id.dataview);

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

            lostMainTape.addView(view, 1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setOnActions() {
        lostScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener(){

            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if(scrollY <= 70) {
                    listener.onLostInputSent(false, scrollY);
                }
                if(scrollY >= 70) {
                    listener.onLostInputSent(true, scrollY);
                }
            }
        });

    }

    private void openFullRequest() {
        if(lostMainTape.getChildCount() > 1) {
            for (int i = 1; i < lostMainTape.getChildCount(); i++) {
                final int index = i;
                lostMainTape.getChildAt(i).findViewById(R.id.constraintlayout).setOnClickListener(new View.OnClickListener() {

                    TextView titleText = lostMainTape.getChildAt(index).findViewById(R.id.title);
                    TextView description = lostMainTape.getChildAt(index).findViewById(R.id.description);
                    TextView whoFind = lostMainTape.getChildAt(index).findViewById(R.id.person_name);
                    TextView data = lostMainTape.getChildAt(index).findViewById(R.id.dataview);
                    TextView category = lostMainTape.getChildAt(index).findViewById(R.id.category);
                    ImageView imageButton = lostMainTape.getChildAt(index).findViewById(R.id.imageView);

                    @Override
                    public void onClick(View v) {
                        launchOpenRequestActivity(titleText.getText().toString(), description.getText().toString(),
                                whoFind.getText().toString(), data.getText().toString(), category.getText().toString(), imageButton);
                    }
                });
            }
        }
    }

    private void launchOpenRequestActivity(String title, String description, String whoFind, String data, String category, ImageView image) {
        Intent request = new Intent(getActivity(), OpenRequest.class);
        request.putExtra("category", category);
        request.putExtra("item", "Lost Item");
        request.putExtra("title", title);
        request.putExtra("description", description);
        request.putExtra("who", whoFind);
        request.putExtra("data", data);
        //request.putExtra("image", image);
        startActivity(request);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof LostFragmentListener) {
            listener = (LostFragmentListener) context;
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
