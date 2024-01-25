package com.shd.ui.activity.main_activity.home;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.shd.R;
import com.shd.halperclass.informationclass.AdminInfo;
import com.shd.halperclass.informationclass.Codes;
import com.shd.viewmodes.AdminModel;
import com.shd.ui.fragments.mainFragments.AddJewelleryFragment;
import com.shd.ui.fragments.mainFragments.HomeFragment;
import com.shd.ui.fragments.mainFragments.ProfilesDetailFragment;
import com.shd.ui.fragments.mainFragments.SearchFragment;
import com.shd.ui.fragments.mainFragments.UserProfileFragment;
import com.shd.viewmodes.DesignCodeModel;
import com.shd.viewmodes.TempCodeModel;

public class HomeActivity extends AppCompatActivity {
    MeowBottomNavigation navigation;
    AdminModel adminModel;
    DesignCodeModel designCodeModel;
    TempCodeModel tempCodeModel;
    final AdminInfo adminInfo = AdminInfo.getInstance();
    final Codes codes = Codes.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        adminModel = new ViewModelProvider(this).get(AdminModel.class);
        adminModel.getAdminMap().observe(this, adminInfo::updateAdminList);

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
        replace(new HomeFragment());

        navigation.setOnClickMenuListener(model -> {
            switch (model.getId())
            {
                case 1 :
                    replace(new HomeFragment());
                    break;
                case 2 :
                    replace(new AddJewelleryFragment());
                    break;
                case 3 :
                    replace(new SearchFragment());
                    break;
                case 4 :
                    replace(new ProfilesDetailFragment());
                    break;
                case 5 :
                    replace(new UserProfileFragment());
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
                    replace(new HomeFragment());
                }
            }
        });
    }

    private void replace(Fragment fragment)
    {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,fragment);
        transaction.commit();
    }

    private boolean isHomeFragment()
    {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        return fragment instanceof HomeFragment;
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        auth.signOut();
//    }
}