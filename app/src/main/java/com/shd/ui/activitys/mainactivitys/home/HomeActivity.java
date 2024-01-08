package com.shd.ui.activitys.mainactivitys.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.shd.R;
import com.shd.ui.fragments.mainfragments.AddJewelleryFragment;
import com.shd.ui.fragments.mainfragments.HomeFragment;
import com.shd.ui.fragments.mainfragments.ProfilesDetailFragment;
import com.shd.ui.fragments.mainfragments.SearchFragment;
import com.shd.ui.fragments.mainfragments.UserProfileFragment;

public class HomeActivity extends AppCompatActivity {
    MeowBottomNavigation navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

    @Override
    public void onBackPressed() {
       if(isHomeFragment())
       {
           super.onBackPressed();
           // TODO : that code is working. after complete Entire app remove comment
//           onDestroy();
       }else {
           navigation.show(1,true);
           replace(new HomeFragment());
       }
    }

// TODO : that code is working. after complete Entire app remove comment

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        auth.signOut();
//    }
}