package com.example.polyfinderv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FoundFragment extends Fragment {
    private View foundView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        foundView = inflater.inflate(R.layout.found_fragment, container, false);
        return foundView;
    }
}
