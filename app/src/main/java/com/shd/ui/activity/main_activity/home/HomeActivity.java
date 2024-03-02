package com.shd.ui.activity.main_activity.home;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.shd.R;
import com.shd.halperclass.informationclass.AdminInfo;
import com.shd.halperclass.informationclass.Codes;
import com.shd.halperclass.tabsadapaters.HomeActivityAdapter;
import com.shd.viewmodes.AdminModel;
import com.shd.viewmodes.DesignCodeModel;
import com.shd.viewmodes.TempCodeModel;

public class HomeActivity extends AppCompatActivity {
    ChipNavigationBar navigation;
    AdminModel adminModel;
    DesignCodeModel designCodeModel;
    TempCodeModel tempCodeModel;
    final AdminInfo adminInfo = AdminInfo.getInstance();
    final Codes codes = Codes.getInstance();
    ViewPager2 viewPager2;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }

        viewPager2 = findViewById(R.id.view_pager2);
        // TODO : that code is working. after complete Entire app remove comment
//        adminModel = new ViewModelProvider(this).get(AdminModel.class);
//        adminModel.getAdminMap().observe(this, adminInfo::updateAdminList);

        designCodeModel = new ViewModelProvider(this).get(DesignCodeModel.class);
        designCodeModel.getDesignCodeList().observe(this, codes::updateDesignCodes);

        tempCodeModel = new ViewModelProvider(this).get(TempCodeModel.class);
        tempCodeModel.getTempCodeList().observe(this, codes::updateTempCodes);

        navigation = findViewById(R.id.bottom_navigation);

        HomeActivityAdapter homeActivityAdapter = new HomeActivityAdapter(this);
        viewPager2.setAdapter(homeActivityAdapter);

        viewPager2.setUserInputEnabled(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.setCurrentItem(0,true);

        navigation.setItemSelected(R.id.home_icon,true);
        navigation.setOnItemSelectedListener(item -> {
            if (item == R.id.home_icon) {
                replace(0);
            } else if (item == R.id.jewellery_icon) {
                replace(1);
            } else if (item == R.id.search_icon) {
                replace(2);
            } else if (item == R.id.upload_icon) {
                replace(3);
            } else if (item == R.id.add_user_icon) {
                replace(4);
            }
        });

        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(isHomeFragment())
                {
                    // TODO : that code is working. after complete Entire app remove comment
//                       onDestroy();
                    finish();
                }else {
                    navigation.setItemSelected(R.id.home_icon,true);
                    replace(0);
                }
            }
        });
    }

    private void replace(int position)
    {
        viewPager2.setCurrentItem(position,true);
    }

    private boolean isHomeFragment()
    {
        ViewPager2 viewPager2 = findViewById(R.id.view_pager2);

        if(viewPager2 == null || viewPager2.getAdapter() == null || viewPager2.getAdapter().getItemCount() == 0) return false;

        int currentFragment = viewPager2.getCurrentItem();
        return currentFragment == 0 ;
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        auth.signOut();
//    }
}