package com.example.polyfinderv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LostFragment extends Fragment {

    private View lostView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lostView = inflater.inflate(R.layout.lost_fragment, container, false);
        return lostView;
    }
}
