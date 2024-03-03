package com.shd.ui.activity.sub_activity.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.archit.calendardaterangepicker.customviews.CalendarListener;
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.textfield.TextInputEditText;
import com.shd.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SearchFilterActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    private boolean isChanging = false,statusValue= false,imgOnlyValue= false;
    private String startDate, endDate,dcStartingCodeValue,dcEndCodeValue,tcStaringCodeValue,tcEndCodeValue,goldStaringValue,goldEndValue,diamondStaringValue,diamondEndValue;
    TextInputEditText dcStartingCode, dcEndCode, tcStaringCode, tcEndCode;
    MaterialSwitch status, imgOnly;
    EditText goldStartingEditText, goldEndEditText, diamondStartingEditText, diamondEndEditText;
    RangeSlider goldSlider, diamondSlider;
    Button applyFilter, removeFilter;
    private final SimpleDateFormat format = new SimpleDateFormat("EEE, MMM dd yyyy", Locale.getDefault());
    List<String> mainList = new ArrayList<>();
    List<String> subList = new ArrayList<>();
    List<String> selectedMainList = new ArrayList<>();
    List<String> selectedSubList = new ArrayList<>();
    DateRangeCalendarView rangeCalendarView;
    ChipGroup mainJewellery, subJewellery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_fileter);

        findIds();
        getJewelleryList();
        setJewelleryChip();

        toolbar.setNavigationOnClickListener(v -> finish());
        rangeCalendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onFirstDateSelected(@NonNull Calendar calendar) {
                startDate = format.format(calendar.getTime());
            }

            @Override
            public void onDateRangeSelected(@NonNull Calendar calendar, @NonNull Calendar calendar1) {
                startDate = format.format(calendar.getTime());
                endDate = format.format(calendar1.getTime());
            }
        });

        mainJewellery.setOnCheckedStateChangeListener((group, checkedIds) -> {
            selectedMainList.clear();
            for (int i : checkedIds) {
                Chip chip = findViewById(i);
                selectedMainList.add((String) chip.getText());
            }
        });

        subJewellery.setOnCheckedStateChangeListener((group, checkedIds) -> {
            selectedSubList.clear();
            for (int i : checkedIds) {
                Chip chip = findViewById(i);
                selectedSubList.add((String) chip.getText());
            }
        });

        goldSlider.addOnChangeListener((slider, value, fromUser) -> {
            setInputText(slider.getValues().get(0), goldStartingEditText);
            setInputText(slider.getValues().get(1), goldEndEditText);
        });

        diamondSlider.addOnChangeListener((slider, value, fromUser) -> {
            setInputText(slider.getValues().get(0), diamondStartingEditText);
            setInputText(slider.getValues().get(1), diamondEndEditText);
        });

        goldStartingEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float value = goldEndEditText.getText().toString().isEmpty() ? 0.00f : Float.parseFloat(goldEndEditText.getText().toString());
               setSlider(goldStartingEditText,goldSlider,value,true);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        goldEndEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float value = goldStartingEditText.getText().toString().isEmpty() ? 0.00f : Float.parseFloat(goldStartingEditText.getText().toString());
                setSlider(goldEndEditText,goldSlider,value,false);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        diamondStartingEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float value = diamondEndEditText.getText().toString().isEmpty() ? 0.00f : Float.parseFloat(diamondEndEditText.getText().toString());
                setSlider(diamondStartingEditText,diamondSlider,value,true);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        diamondEndEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float value = diamondStartingEditText.getText().toString().isEmpty() ? 0.00f : Float.parseFloat(diamondStartingEditText.getText().toString());
                setSlider(diamondEndEditText,diamondSlider,value,true);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        
        removeFilter.setOnClickListener(v->clearFilter());
        
        applyFilter.setOnClickListener(v-> getFilter());
    }
    
    private void findIds() {
        toolbar = findViewById(R.id.filter_appbar_material);
        dcStartingCode = findViewById(R.id.filter_start_design_code_input_text);
        dcEndCode = findViewById(R.id.filter_end_design_code_input_text);
        tcStaringCode = findViewById(R.id.filter_start_temp_code_input_text);
        tcEndCode = findViewById(R.id.filter_end_temp_code_input_text);
        status = findViewById(R.id.filter_status_switch);
        imgOnly = findViewById(R.id.filter_img_switch);
        rangeCalendarView = findViewById(R.id.filter_calender);
        mainJewellery = findViewById(R.id.filter_main_type_group);
        subJewellery = findViewById(R.id.filter_sub_type_group);
        goldStartingEditText = findViewById(R.id.filter_gold_starting_value_input_text);
        goldEndEditText = findViewById(R.id.filter_gold_end_value_input_text);
        goldSlider = findViewById(R.id.filter_gold_slider);
        diamondStartingEditText = findViewById(R.id.filter_diamond_starting_value_input_text);
        diamondEndEditText = findViewById(R.id.filter_diamond_end_value_input_text);
        diamondSlider = findViewById(R.id.filter_diamond_slider);
        applyFilter = findViewById(R.id.filter_apply_button);
        removeFilter = findViewById(R.id.filter_remove_button);
    }

    private void getJewelleryList() {
        String[] jewelleryList;
        mainList.addAll(Arrays.asList(getResources().getStringArray(R.array.jewellery_main_type)));
        jewelleryList = getResources().getStringArray(R.array.jewellery_main_type);
        for (String jewellery : jewelleryList) {
            if (!mainList.contains(jewellery)) mainList.add(jewellery);
        }

        jewelleryList = getResources().getStringArray(R.array.jewellery_ring_sub_type);
        for (String jewellery : jewelleryList) {
            if (!subList.contains(jewellery)) subList.add(jewellery);
        }

        jewelleryList = getResources().getStringArray(R.array.jewellery_nkc_sub_type);
        for (String jewellery : jewelleryList) {
            if (!subList.contains(jewellery)) subList.add(jewellery);
        }

        jewelleryList = getResources().getStringArray(R.array.jewellery_er_sub_type);
        for (String jewellery : jewelleryList) {
            if (!subList.contains(jewellery)) subList.add(jewellery);
        }

        jewelleryList = getResources().getStringArray(R.array.jewellery_br_sub_type);
        for (String jewellery : jewelleryList) {
            if (!subList.contains(jewellery)) subList.add(jewellery);
        }
    }

    @SuppressLint("InflateParams")
    private void setJewelleryChip() {
        int id = 0;
        for (String s : mainList) {
            Chip chip = (Chip) LayoutInflater.from(SearchFilterActivity.this).inflate(R.layout.chip_layout, null);
            chip.setText(s);
            chip.setId(id);
            id++;
            mainJewellery.addView(chip);
        }

        id = 0;
        for (String s : subList) {
            Chip chip = (Chip) LayoutInflater.from(SearchFilterActivity.this).inflate(R.layout.chip_layout, null);
            chip.setText(s);
            chip.setId(id);
            id++;
            subJewellery.addView(chip);
        }
    }

    private void setInputText(float sliderValue, EditText editText) {
        if (!isChanging) {
            isChanging = true;
            float formComponent = (float) (Math.ceil(sliderValue * 100) / 100);
            editText.setText(String.valueOf(formComponent));
            isChanging = false;
        }
    }

    private void setSlider(EditText editText,RangeSlider slider,float value,boolean isStartingValue) {
        String number = Objects.requireNonNull(editText.getText()).toString();
        if (!isChanging) {
            isChanging = true;
            if (isStartingValue) {
                if (number.isEmpty()) {
                    slider.setValues(0.00f, value);
                    editText.setHint("0.0");
                } else if (number.equals(".")) {
                    number = "0.";
                    editText.setText(number);
                    editText.setSelection(editText.getText().length());
                } else if (number.equals("0")) {
                    number = "";
                    editText.setText(number);
                    editText.setSelection(editText.getText().length());
                } else {
                    if (number.matches("^(\\d+\\.\\d{1,2}|\\d+|\\d+\\.)$")) {
                        float sliderValue = Float.parseFloat(number);
                        if (sliderValue >= 0.00 && sliderValue <= 100.00) {
                            if (sliderValue <= value) {
                                slider.setValues(sliderValue, value);
                                editText.setSelection(editText.getText().length());
                            } else {
                                slider.setValues(value, value);
                                editText.setSelection(editText.getText().length());
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.slider_error1), Toast.LENGTH_SHORT).show();
                            slider.setValues(0.00f, value);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.slider_error2), Toast.LENGTH_SHORT).show();
                        slider.setValues(0.00f, value);
                    }
                }
            } else {
                if (number.isEmpty()) {
                    slider.setValues(value, value);
                    editText.setHint("0.0");
                } else if (number.equals(".")) {
                    number = "0.";
                    editText.setText(number);
                    editText.setSelection(editText.getText().length());
                } else if (number.equals("0")) {
                    number = "";
                    editText.setText(number);
                    editText.setSelection(editText.getText().length());
                } else {
                    if (number.matches("^(\\d+\\.\\d{1,2}|\\d+|\\d+\\.)$")) {
                        float sliderValue = Float.parseFloat(number);
                        if (sliderValue >= 0.00 && sliderValue <= 100.00) {
                            if (sliderValue >= value) {
                                slider.setValues(value, sliderValue);
                                editText.setSelection(editText.getText().length());
                            } else {
                                slider.setValues(value, value);
                                editText.setSelection(editText.getText().length());
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.slider_error1), Toast.LENGTH_SHORT).show();
                            slider.setValues(value, value);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.slider_error2), Toast.LENGTH_SHORT).show();
                        slider.setValues(value, value);
                    }
                }
            }
            isChanging = false;
        }
    }

    private void clearFilter() {
        dcStartingCode.setText("");
        dcEndCode.setText("");
        tcStaringCode.setText("");
        tcEndCode.setText("");
        status.setChecked(false);
        imgOnly.setChecked(false);
        rangeCalendarView.resetAllSelectedViews();
        resetChips();
        goldSlider.setValues(0.00f,0.00f);
        goldStartingEditText.setText("");
        goldEndEditText.setText("");
        diamondSlider.setValues(0.00f,0.00f);
        diamondStartingEditText.setText("");
        diamondEndEditText.setText("");
    }

    private void resetChips() {
        for (int i = 0; i < mainJewellery.getChildCount(); i++) {
            View child = mainJewellery.getChildAt(i);
            if (child instanceof Chip) {
                Chip chip = (Chip) child;
                chip.setChecked(false);
            }
        }
        for (int i = 0; i < subJewellery.getChildCount(); i++) {
            View child = subJewellery.getChildAt(i);
            if (child instanceof Chip) {
                Chip chip = (Chip) child;
                chip.setChecked(false);
            }
        }
    }

    private void getFilter() {
        statusValue = status.isChecked();
        imgOnlyValue = imgOnly.isChecked();
        dcStartingCodeValue = Objects.requireNonNull(dcStartingCode.getText()).toString();
        dcEndCodeValue = Objects.requireNonNull(dcEndCode.getText()).toString();
        tcStaringCodeValue = Objects.requireNonNull(tcStaringCode.getText()).toString();
        tcEndCodeValue = Objects.requireNonNull(tcEndCode.getText()).toString();
        goldStaringValue = Objects.requireNonNull(goldStartingEditText.getText()).toString();
        goldEndValue = Objects.requireNonNull(goldEndEditText.getText()).toString();
        diamondStaringValue = Objects.requireNonNull(diamondStartingEditText.getText()).toString();
        diamondEndValue = Objects.requireNonNull(diamondEndEditText.getText()).toString();


        Log.d("filter Data","Status : " + statusValue);
        Log.d("filter Data","img only : " + imgOnlyValue);
        Log.d("filter Data","Design code");
        Log.d("filter Data","start : " + dcStartingCodeValue);
        Log.d("filter Data","End : " + dcEndCodeValue);
        Log.d("filter Data","Temp Code" );
        Log.d("filter Data","start : " + tcStaringCodeValue);
        Log.d("filter Data","End : " + tcEndCodeValue);
        Log.d("filter Data","Date : ");
        Log.d("filter Data","start : " + startDate);
        Log.d("filter Data","End : " + endDate);
        Log.d("filter Data","Jewellery type ");
        Log.d("filter Data","Main : ");
        selectedMainList.forEach(s -> Log.d("filter Data", s));
        Log.d("filter Data","Sub  : ");
        selectedSubList.forEach(s -> Log.d("filter Data", s));
        Log.d("filter Data","Gold ");
        Log.d("filter Data","start : " + goldStaringValue);
        Log.d("filter Data","End : " + goldEndValue);
        Log.d("filter Data","Diamond ");
        Log.d("filter Data","start : " + diamondStaringValue);
        Log.d("filter Data","End : " + diamondEndValue);
    }
}