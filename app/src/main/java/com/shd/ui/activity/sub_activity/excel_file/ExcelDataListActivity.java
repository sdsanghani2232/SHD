package com.shd.ui.activity.sub_activity.excel_file;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.shd.R;
import com.shd.halperclass.recyclerviewAdapters.ExcelDataRvAdapter;
import com.shd.halperclass.validationclass.ExcelDataValidation;
import com.shd.viewmodes.ExcelFileData;
import java.util.List;

public class ExcelDataListActivity extends AppCompatActivity implements ExcelDataRvAdapter.DeleteItemListener {

    MaterialToolbar toolbar;
    Button save;
    TextView totalDesign, totalErrorDesign;
    RecyclerView recyclerView;
    CircularProgressIndicator progressbar;
    boolean exit = false, hasNextList = true,getData = false;
    private ExcelDataRvAdapter adapter;
    private ShimmerFrameLayout shimmerView;
    private NestedScrollView nestedScrollView;
    private int currentPosition = 0,currentItems = 0;
    List<List<Object>> sheetData = ExcelFileData.getInstance().getExcelDataList();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel_data_list);
        findIds();
        LinearLayoutManager manager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(manager);

        if (sheetData.size() == 0) {
            totalDesign.setVisibility(View.GONE);
            toolbar.getMenu().getItem(0).setVisible(false);
            toolbar.getMenu().getItem(0).setCheckable(false);
        }
        else totalDesign.setText(getResources().getString(R.string.total_design_count) + sheetData.size());

        ExcelDataValidation validation = new ExcelDataValidation(this);
        int errorCount = validation.validateData();
        if (errorCount == 0) {
            totalErrorDesign.setVisibility(View.GONE);
        } else {
            totalErrorDesign.setText(getResources().getString(R.string.total_error_count) + errorCount);
        }
        shimmerView.startShimmer();

        adapter = new ExcelDataRvAdapter(this, totalDesign, totalErrorDesign, save,getStartingList(), errorCount,this);

        new Handler().postDelayed(() -> {
            shimmerView.stopShimmer();
            shimmerView.setVisibility(View.GONE);
            recyclerView.setAdapter(adapter);
            nestedScrollView.setVisibility(View.VISIBLE);
        }, 2000);

        toolbar.setNestedScrollingEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (!exit) {
                    Toast.makeText(ExcelDataListActivity.this, getResources().getString(R.string.one_more_time), Toast.LENGTH_SHORT).show();
                    exit = true;
                    new Handler().postDelayed(() -> exit = false, 5000);
                } else finish();
            }
        });

        save.setOnClickListener(v ->
        {
            adapter.storeData();
            save.setClickable(false);
        });

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                if(hasNextList && !getData)
                {
                    getData = true;
                    progressbar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(() ->
                    {
                        progressbar.setVisibility(View.GONE);
                        adapter.addList(getNextList());
                        currentPosition += 20;
                        currentItems += 10;
                        getData = false;
                    }, 2000);

                }
            }
        });

    }

    private void findIds() {
        toolbar = findViewById(R.id.excel_appbar_material);
        save = findViewById(R.id.excel_data_save_button);
        totalDesign = findViewById(R.id.excel_file_count);
        totalErrorDesign = findViewById(R.id.excel_file_error_count);
        recyclerView = findViewById(R.id.excel_file_rv);
        shimmerView = findViewById(R.id.excel_file_shimmer_view);
        nestedScrollView = findViewById(R.id.excel_file_nested_scrollview);
        progressbar = findViewById(R.id.excel_file_progress_indicator);
    }

    private List<List<Object>> getStartingList() {
        if (sheetData.size() < 10) {
            hasNextList = false;
            return sheetData.subList(0, sheetData.size());
        } else {
            currentPosition += 10;
            currentItems += 10;
            return sheetData.subList(0, currentPosition);
        }
    }

    private List<List<Object>> getNextList() {

        if ((currentPosition + 10) < sheetData.size()) {
            return sheetData.subList(currentPosition ,(currentPosition +10));
        }else {
            hasNextList = false;
            return sheetData.subList(currentPosition, sheetData.size());
        }
    }

    @Override
    public void deleteItem() {
        currentPosition -= 1;
        currentItems -= 1;
        if(currentItems < 5 && hasNextList) {
            adapter.addList(getNextList());
            currentItems += 10;
            currentPosition +=20;
        }
    }
}