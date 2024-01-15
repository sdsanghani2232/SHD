package com.shd.halperclass.tabsadapaters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.shd.ui.fragments.subFragments.addJewellery.ExcelFragment;
import com.shd.ui.fragments.subFragments.addJewellery.FormFragment;

public class AddJewelleryTabAdapter extends FragmentStateAdapter {
    public AddJewelleryTabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0 :
                return new FormFragment();
            case 1:
                return new ExcelFragment();
        }
        return new FormFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
