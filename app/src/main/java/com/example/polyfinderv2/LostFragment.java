package com.example.polyfinderv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class LostFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View lostView = inflater.inflate(R.layout.lost_fragment, container, false);

        ScrollView lostScrollView = lostView.findViewById(R.id.lostscrollView);
        LinearLayout lostMainTape = lostScrollView.findViewById(R.id.lostmainTape);

        Bundle bundle = getArguments();

        if(bundle != null) {

            View view = getLayoutInflater().inflate(R.layout.request_rectangle, null);

            EditText title = view.findViewById(R.id.title);
            EditText description = view.findViewById(R.id.description);

            String titleText = bundle.getString("title");
            String descText = bundle.getString("description");

            title.setText(titleText);
            title.setLines(1);
            title.setEnabled(false);

            description.setText(descText);
            description.setLines(2);
            description.setEnabled(false);

            lostMainTape.addView(view, 0);
        }

        return lostView;
    }
}
