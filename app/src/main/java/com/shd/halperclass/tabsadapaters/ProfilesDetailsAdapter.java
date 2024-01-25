package com.shd.halperclass.tabsadapaters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.shd.ui.fragments.subFragments.profileDetail.CustomerFragment;
import com.shd.ui.fragments.subFragments.profileDetail.EmploysFragment;

public class ProfilesDetailsAdapter extends FragmentStateAdapter {
    public ProfilesDetailsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0 :
                return new EmploysFragment();
            case 1:
                return new CustomerFragment();
        }
        return new EmploysFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
