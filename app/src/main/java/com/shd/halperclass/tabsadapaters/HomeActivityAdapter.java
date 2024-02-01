package com.shd.halperclass.tabsadapaters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.shd.ui.fragments.mainFragments.AddJewelleryFragment;
import com.shd.ui.fragments.mainFragments.HomeFragment;
import com.shd.ui.fragments.mainFragments.ProfilesDetailFragment;
import com.shd.ui.fragments.mainFragments.SearchFragment;
import com.shd.ui.fragments.mainFragments.UserProfileFragment;
public class HomeActivityAdapter extends FragmentStateAdapter {

    public HomeActivityAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1:
                return   new AddJewelleryFragment();
            case 2:
                return new SearchFragment();

            case 3:
                return new ProfilesDetailFragment();

            case 4 :
                return   new UserProfileFragment();

            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
