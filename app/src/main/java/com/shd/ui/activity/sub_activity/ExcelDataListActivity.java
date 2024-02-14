package com.shd.ui.activity.sub_activity;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.shd.R;
import com.shd.halperclass.recyclerviewAdapters.ExcelDataRvAdapter;
import com.shd.viewmodes.ExcelFileData;

import java.util.ArrayList;
import java.util.List;

public class ExcelDataListActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    Button save;
    TextView totalDesign,totalErrorDesign;
    RecyclerView recyclerView;
    ExcelFileData excelFileData = ExcelFileData.getInstance();
    List<List<Object>> data = new ArrayList<>();
    boolean exit = false;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel_data_list);
        findIds();

        data = excelFileData.getExcelDataList();

        ExcelDataRvAdapter adapter = new ExcelDataRvAdapter(totalDesign,totalErrorDesign,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        toolbar.setNestedScrollingEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if(!exit)
                {
                    Toast.makeText(ExcelDataListActivity.this, "one more time if you wont to exit", Toast.LENGTH_SHORT).show();
                    exit = true;
                    new Handler().postDelayed(() -> exit = false,5000);
                }else finish();
            }
        });

        save.setOnClickListener(v->
        {
            adapter.storeData();
            save.setClickable(false);
        });

    }

    private void findIds() {
        toolbar = findViewById(R.id.excel_appbar_material);
        save = findViewById(R.id.excel_data_save_button);
        totalDesign = findViewById(R.id.excel_file_count);
        totalErrorDesign = findViewById(R.id.excel_file_error_count);
        recyclerView = findViewById(R.id.excel_file_rv);
    }
}