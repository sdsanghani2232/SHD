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

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.shd.R;
import com.shd.halperclass.informationclass.AdminInfo;
import com.shd.halperclass.informationclass.Codes;
import com.shd.halperclass.tabsadapaters.HomeActivityAdapter;
import com.shd.viewmodes.AdminModel;
import com.shd.viewmodes.DesignCodeModel;
import com.shd.viewmodes.TempCodeModel;

public class HomeActivity extends AppCompatActivity {
    MeowBottomNavigation navigation;
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
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
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

        navigation.add(new MeowBottomNavigation.Model(1,R.drawable.home));
        navigation.add(new MeowBottomNavigation.Model(2,R.drawable.folderplus));
        navigation.add(new MeowBottomNavigation.Model(3,R.drawable.search));
        navigation.add(new MeowBottomNavigation.Model(4,R.drawable.add_user));
        navigation.add(new MeowBottomNavigation.Model(5,R.drawable.profile));

        navigation.show(1,true);

        HomeActivityAdapter homeActivityAdapter = new HomeActivityAdapter(this);
        viewPager2.setAdapter(homeActivityAdapter);

        viewPager2.setUserInputEnabled(false);
        viewPager2.setOffscreenPageLimit(1);
        viewPager2.setCurrentItem(0);

        navigation.setOnClickMenuListener(model -> {
            switch (model.getId())
            {
                case 1 :
                    replace(0);
                    break;
                case 2 :
                    replace(1);
                    break;
                case 3 :
                    replace(2);
                    break;
                case 4 :
                    replace(3);
                    break;
                case 5 :
                    replace(4);
                    break;

            }
            return null;
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
                    navigation.show(1,true);
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