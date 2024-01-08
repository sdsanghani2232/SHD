package com.shd.ui.fragments.mainfragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.shd.R;
import com.shd.halperclass.tabsadapaters.AddJewelleryTabAdapter;

public class AddJewelleryFragment extends Fragment {

    TabLayout addJewellerytablayout;
    ViewPager2 addJewelleryViewPager;

    public AddJewelleryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_jewellery, container, false);

        addJewellerytablayout = view.findViewById(R.id.add_jewellery_tab_view);
        addJewelleryViewPager = view.findViewById(R.id.add_jewellery_viewpager);
        AddJewelleryTabAdapter addJewelleryTabAdapter = new AddJewelleryTabAdapter(requireActivity());
        addJewelleryViewPager.setAdapter(addJewelleryTabAdapter);

        new TabLayoutMediator(addJewellerytablayout, addJewelleryViewPager, (tab, position) -> {
            switch (position)
            {
                case 0 :
                    tab.setText("FORM");
                    break;
                case 1 :
                    tab.setText("EXCEL");
                    break;
            }
        }).attach();

        return view;
    }
}