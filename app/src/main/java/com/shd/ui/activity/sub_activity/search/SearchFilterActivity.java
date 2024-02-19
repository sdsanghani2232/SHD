package com.shd.ui.activity.sub_activity.search;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;
import com.google.android.material.appbar.MaterialToolbar;
import com.shd.R;

public class SearchFilterActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_fileter);
        findIds();

//        set App bar icons
        Menu m = toolbar.getMenu();
        m.getItem(0).setVisible(false).setCheckable(false);
        m.getItem(1).setVisible(false).setCheckable(false);
        m.getItem(2).setCheckable(false).setCheckable(false);

        toolbar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.more_menu)
            {
                Toast.makeText(this, "more", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    private void findIds() {
        toolbar = findViewById(R.id.filter_appbar_material);
    }
}