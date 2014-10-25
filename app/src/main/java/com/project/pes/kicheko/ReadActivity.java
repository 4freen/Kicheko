package com.project.pes.kicheko;

/**
 * Created by apple on 25/10/14.
 */
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;

public class ReadActivity extends Fragment {

    public ReadActivity(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_read, container, false);

        return rootView;
    }
}