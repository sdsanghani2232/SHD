package com.shd.ui.fragments.mainFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.shd.R;
import com.shd.ui.activity.sub_activity.search.SearchFilterActivity;

public class SearchDataFragment extends Fragment {

    TextInputEditText searchBar;
    FloatingActionButton filter;
    public SearchDataFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        findId(view);

        filter.setOnClickListener(v-> startActivity(new Intent(requireContext(), SearchFilterActivity.class)));

        return view;
    }

    private void findId(View view) {
        searchBar = view.findViewById(R.id.search_design_input);
        filter = view.findViewById(R.id.search_filter_button);
    }
}