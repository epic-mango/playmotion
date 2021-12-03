package com.aldeanos.playmotion.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aldeanos.playmotion.R;
import com.aldeanos.playmotion.databinding.FragmentExplorarBinding;

public class ExplorarFragment extends Fragment {

    FragmentExplorarBinding binding;

    public ExplorarFragment() {

    }


    public static ExplorarFragment newInstance(String param1, String param2) {
        ExplorarFragment fragment = new ExplorarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      binding = FragmentExplorarBinding.inflate(inflater, container,false);

      return binding.getRoot();
    }
}