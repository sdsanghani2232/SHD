package com.shd.ui.fragments.mainFragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.shd.R;
import com.shd.halperclass.tabsadapaters.ProfilesDetailsAdapter;

public class ProfilesDetailFragment extends Fragment {
    TabLayout profilestablayout;
    ViewPager2 profilesViewPager;

    public ProfilesDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profiles_detail, container, false);

        profilestablayout = view.findViewById(R.id.profile_details_tab_view);
        profilesViewPager = view.findViewById(R.id.profile_details_viewpager);

        ProfilesDetailsAdapter profilesDetailsAdapter = new ProfilesDetailsAdapter(requireActivity());
        profilesViewPager.setAdapter(profilesDetailsAdapter);
        profilesViewPager.setUserInputEnabled(false);

        new TabLayoutMediator(profilestablayout, profilesViewPager, (tab, position) -> {
            switch (position)
            {
                case 0 :
                    tab.setText("EMPLOYEES");
                    break;
                case 1 :
                    tab.setText("CUSTOMER");
                    break;
            }
        }).attach();
        return view;
    }
}