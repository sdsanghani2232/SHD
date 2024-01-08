package com.shd.halperclass.tabsadapaters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.shd.ui.fragments.subfragments.addjewellery.ExcelFragment;
import com.shd.ui.fragments.subfragments.addjewellery.FormFragment;
import com.shd.ui.fragments.subfragments.profiledetail.CustomerFragment;
import com.shd.ui.fragments.subfragments.profiledetail.EmployesFragment;

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
                return new EmployesFragment();
            case 1:
                return new CustomerFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
