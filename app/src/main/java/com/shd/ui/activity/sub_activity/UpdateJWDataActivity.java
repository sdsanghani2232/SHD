package com.shd.ui.activity.sub_activity;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.Toast;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shd.R;
import com.shd.halperclass.informationclass.Codes;
import com.shd.halperclass.validationclass.FormValidation;
import com.shd.repository.readdata.DesignCodeList;
import com.shd.repository.readdata.TempCodeList;
import com.shd.viewmodes.ExcelFileData;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class UpdateJWDataActivity extends AppCompatActivity {

    private boolean isChanging = false, isError = false, status = false;
    private String gold_weight = "0.0", diamond_weight = "0.0", length = "0.0", width = "0.0", height = "0.0", designCode, mainType, subType, customerCode, tempCode, customerName, workBy, workPlace, selectedDate;
    private int positionOfUpdate;
    ScrollView mainScrollView;
    private final Codes codes = Codes.getInstance();
    private final TempCodeList tempCodeList = TempCodeList.getInstance();
    private final DesignCodeList designCodeList = DesignCodeList.getInstance();
    private final ExcelFileData excelFileData = ExcelFileData.getInstance();
    List<List<Object>> mainList = excelFileData.getExcelDataList();
    List<Object> designList;
    Bitmap img1Bitmap,img2Bitmap;
    MaterialToolbar appbar;
    View indicator1, indicator2;
    ShapeableImageView jewelleryImg1, jewelleryImg2;
    MaterialSwitch designStatus;
    FloatingActionButton jewelleryImg1Button, jewelleryImg2Button;
    HorizontalScrollView imgScrollView;
    TextInputLayout date_layout, jewelleryMainTypeLayout, jewellerySubTypeLayout, gold_layout, diamond_layout, length_layout, width_layout, height_layout, design_code_layout, customer_code_layout, temp_code_layout, errorLayout;
    TextInputEditText date_text, gold_text, diamond_text, length_text, width_text, height_text, design_code_text, customer_text, work_by_text, work_place_text, customer_code_text, temp_code_text;
    MaterialAutoCompleteTextView jewelleryMainTypeText, jewellerySubTypeText;
    Slider gold_slider, diamond_slider, length_slider, width_slider, height_slider;
    Button save;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_jwdata);
        
        findIds();

        positionOfUpdate = getIntent().getIntExtra("position",0);
        designList = mainList.get(positionOfUpdate);
        setListData();
        setMainDropDownList();

        jewelleryImg1Button.setOnClickListener(v -> img1.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)));
        jewelleryImg2Button.setOnClickListener(v -> img2.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)));
        date_layout.setStartIconOnClickListener(v -> setDate());
        date_text.setOnClickListener(v -> setDate());
        imgScrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> setIndicator(scrollX));
        jewelleryMainTypeText.setOnItemClickListener((parent, view1, position, id) -> {
            jewelleryMainTypeLayout.setFocusable(true);
            jewelleryMainTypeLayout.setErrorEnabled(false);
            setSubDropDown(parent.getItemAtPosition(position).toString());
        });
        gold_slider.addOnChangeListener((slider, value, fromUser) -> gold_weight = setInputText(value, gold_text, gold_layout));
        diamond_slider.addOnChangeListener((slider, value, fromUser) -> diamond_weight = setInputText(value, diamond_text, diamond_layout));
        length_slider.addOnChangeListener((slider, value, fromUser) -> length = setInputText(value, length_text, length_layout));
        width_slider.addOnChangeListener((slider, value, fromUser) -> width = setInputText(value, width_text, width_layout));
        height_slider.addOnChangeListener((slider, value, fromUser) -> height = setInputText(value, height_text, height_layout));

        gold_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                gold_text.setSelection(Objects.requireNonNull(gold_text.getText()).length());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                gold_weight = setSlider(gold_text, gold_layout, gold_slider);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        diamond_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                diamond_text.setSelection(Objects.requireNonNull(diamond_text.getText()).length());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                diamond_weight = setSlider(diamond_text, diamond_layout, diamond_slider);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        length_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                length_text.setSelection(Objects.requireNonNull(length_text.getText()).length());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                length = setSlider(length_text, length_layout, length_slider);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        width_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                width_text.setSelection(Objects.requireNonNull(width_text.getText()).length());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                width = setSlider(width_text, width_layout, width_slider);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        height_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                height_text.setSelection(Objects.requireNonNull(height_text.getText()).length());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                height = setSlider(height_text, height_layout, height_slider);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        save.setOnClickListener(v -> validateData());
        appbar.setNestedScrollingEnabled(true);
        appbar.setNavigationOnClickListener(v->{
            startActivity(new Intent(this, ExcelDataListActivity.class));
            finish();
        });

        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                startActivity(new Intent(getApplicationContext(), ExcelDataListActivity.class));
                finish();
            }
        });
    }
    private void findIds() {
        appbar = findViewById(R.id.update_appbar_material);
        mainScrollView = findViewById(R.id.update_main_scroll_view);
        indicator1 = findViewById(R.id.update_img_indicator_1);
        indicator2 = findViewById(R.id.update_img_indicator_2);
        imgScrollView = findViewById(R.id.update_horizontal_scroll_bar);
        date_layout = findViewById(R.id.update_date_layout);
        date_text = findViewById(R.id.update_date_display_text);
        jewelleryMainTypeLayout = findViewById(R.id.update_jewellery_main_type_layout);
        jewelleryMainTypeText = findViewById(R.id.update_jewellery_main_type_drop_down);
        jewellerySubTypeLayout = findViewById(R.id.update_jewellery_sub_type_layout);
        jewellerySubTypeText = findViewById(R.id.update_jewellery_sub_type_drop_down);
        gold_slider = findViewById(R.id.update_gold_slider);
        gold_text = findViewById(R.id.update_gold_input_text);
        gold_layout = findViewById(R.id.update_gold_text_layout);
        diamond_layout = findViewById(R.id.update_diamond_text_layout);
        diamond_text = findViewById(R.id.update_diamond_input_text);
        diamond_slider = findViewById(R.id.update_diamond_slider);
        length_layout = findViewById(R.id.update_length_text_layout);
        length_text = findViewById(R.id.update_length_input_text);
        length_slider = findViewById(R.id.update_length_slider);
        width_layout = findViewById(R.id.update_width_text_layout);
        width_text = findViewById(R.id.update_width_input_text);
        width_slider = findViewById(R.id.update_width_slider);
        height_layout = findViewById(R.id.update_height_text_layout);
        height_text = findViewById(R.id.update_height_input_text);
        height_slider = findViewById(R.id.update_height_slider);
        save = findViewById(R.id.update_save_button);
        design_code_layout = findViewById(R.id.design_code_layout);
        design_code_text = findViewById(R.id.design_code_text);
        jewelleryImg1Button = findViewById(R.id.update_img_pencil_1);
        jewelleryImg2Button = findViewById(R.id.update_img_pencil_2);
        jewelleryImg1 = findViewById(R.id.update_img_1);
        jewelleryImg2 = findViewById(R.id.update_img_2);
        customer_text = findViewById(R.id.update_customer_input_text);
        work_by_text = findViewById(R.id.update_work_by_input_text);
        work_place_text = findViewById(R.id.update_work_place_input_text);
        designStatus = findViewById(R.id.update_status_switch);
        customer_code_layout = findViewById(R.id.update_customer_code_layout);
        customer_code_text = findViewById(R.id.update_customer_code_input_text);
        temp_code_layout = findViewById(R.id.update_temp_code_layout);
        temp_code_text = findViewById(R.id.update_temp_code_input_text);
    }

    private void setListData() {
        if (!designList.get(0).equals("null")) {
            jewelleryImg1.setImageBitmap((Bitmap) designList.get(0));
            img1Bitmap = (Bitmap) designList.get(0);
        }
        if (!designList.get(1).equals("null")) {
            jewelleryImg2.setImageBitmap((Bitmap) designList.get(1));
            img2Bitmap = (Bitmap) designList.get(1);
        }
        customer_text.setText(designList.get(2).equals("null") ? "" : (CharSequence) designList.get(2));
        design_code_text.setText(designList.get(3).equals("null") ? "" : (CharSequence) designList.get(3));
        customer_code_text.setText(designList.get(4).equals("null") ? "" : (CharSequence) designList.get(4));
        temp_code_text.setText(designList.get(5).equals("null") ? "" : (CharSequence) designList.get(5));
        work_by_text.setText((CharSequence) designList.get(6));
        work_place_text.setText((CharSequence) designList.get(7));
        date_text.setText((CharSequence) designList.get(8));
        jewelleryMainTypeText.setText(designList.get(9).equals("null") ? "" : (CharSequence) designList.get(9));
        jewellerySubTypeText.setText(designList.get(10).equals("null") ? "" : (CharSequence) designList.get(10));
        designStatus.setChecked(designList.get(11).equals("true"));
        length_text.setText(designList.get(12).equals("null") ?String.valueOf(0.00f): String.valueOf(designList.get(12)));
        width_text.setText(designList.get(13).equals("null") ?String.valueOf(0.00f): String.valueOf(designList.get(13)));
        height_text.setText(designList.get(14).equals("null") ?String.valueOf(0.00f): String.valueOf(designList.get(14)));
        gold_text.setText(designList.get(15).equals("null") ?String.valueOf(0.00f): String.valueOf(designList.get(15)));
        diamond_text.setText(designList.get(16).equals("null") ?String.valueOf(0.00f): String.valueOf(designList.get(16)));
        length_slider.setValue(designList.get(12).equals("null") ? 0.00f : Float.parseFloat((String) designList.get(12)));
        width_slider.setValue(designList.get(13).equals("null") ? 0.00f : Float.parseFloat((String) designList.get(13)));
        height_slider.setValue(designList.get(14).equals("null") ? 0.00f : Float.parseFloat((String) designList.get(14)));
        gold_slider.setValue(designList.get(15).equals("null") ? 0.00f : Float.parseFloat((String) designList.get(15)));
        diamond_slider.setValue(designList.get(16).equals("null") ? 0.00f : Float.parseFloat((String) designList.get(16)));
    }

    public final ActivityResultLauncher<Intent> img1 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK || result.getData() != null) {
                assert result.getData() != null;
                try {
                    img1Bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Objects.requireNonNull(result.getData().getData())));
                    jewelleryImg1.setImageBitmap(img1Bitmap);
                } catch (FileNotFoundException e) {
                   e.printStackTrace();
                }
            }
        }
    });
    public final ActivityResultLauncher<Intent> img2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK || result.getData() != null) {
                assert result.getData() != null;
                try {
                    img2Bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Objects.requireNonNull(result.getData().getData())));
                    jewelleryImg2.setImageBitmap(img2Bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    private void setIndicator(int scrollX) {
        int maxScrollX = imgScrollView.getChildAt(0).getWidth() - imgScrollView.getWidth();
        float scroll = (float) scrollX / maxScrollX * 100;

        if (scroll < 50) {
            indicator1.setBackgroundResource(R.drawable.img_scroll_indicator_selected);
            indicator2.setBackgroundResource(R.drawable.img_scroll_indicator_not_selected);
        } else {
            indicator1.setBackgroundResource(R.drawable.img_scroll_indicator_not_selected);
            indicator2.setBackgroundResource(R.drawable.img_scroll_indicator_selected);
        }
    }
    private void setDate() {
        CalendarConstraints.Builder builder = new CalendarConstraints.Builder();
        builder.setValidator(DateValidatorPointBackward.now());

        MaterialDatePicker.Builder<Long> dateBuilder = MaterialDatePicker.Builder.datePicker();
        dateBuilder.setTheme(R.style.Theme_SHD_form_calender);
        dateBuilder.setCalendarConstraints(builder.build());

        long today = MaterialDatePicker.todayInUtcMilliseconds();
        dateBuilder.setSelection(today);

        MaterialDatePicker<Long> datePicker = dateBuilder.build();

        dateBuilder.setTitleText(getResources().getString(R.string.select_date));
        dateBuilder.setSelection(System.currentTimeMillis());

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendars = Calendar.getInstance();
            calendars.setTimeInMillis(selection);
            SimpleDateFormat format = new SimpleDateFormat("EEE, MMM dd yyyy", Locale.getDefault());
            date_text.setText(format.format(calendars.getTime()));
        });

        datePicker.show(getSupportFragmentManager(), "Jewellery Date");
    }
    private void setMainDropDownList() {
        ArrayAdapter<CharSequence> mainList = ArrayAdapter.createFromResource(this, R.array.jewellery_main_type, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        jewelleryMainTypeText.setAdapter(mainList);
        jewellerySubTypeLayout.setEnabled(false);
        jewellerySubTypeText.setEnabled(false);
    }

    private void setSubDropDown(String subType) {
        List<String> mainList = Arrays.asList(getResources().getStringArray(R.array.jewellery_main_type));

        if (subType.equals(mainList.get(0))) {
            jewellerySubTypeLayout.setEnabled(true);
            jewellerySubTypeText.setEnabled(true);
            jewellerySubTypeText.setText(getResources().getString(R.string.sub_type));
            ArrayAdapter<CharSequence> subList = ArrayAdapter.createFromResource(this, R.array.jewellery_ring_sub_type, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
            jewellerySubTypeText.setAdapter(subList);
        } else if (subType.equals(mainList.get(1))) {
            jewellerySubTypeLayout.setEnabled(true);
            jewellerySubTypeText.setEnabled(true);
            jewellerySubTypeText.setText(getResources().getString(R.string.sub_type));
            ArrayAdapter<CharSequence> subList = ArrayAdapter.createFromResource(this, R.array.jewellery_nkc_sub_type, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
            jewellerySubTypeText.setAdapter(subList);
        } else if (subType.equals(mainList.get(2))) {
            jewellerySubTypeLayout.setEnabled(true);
            jewellerySubTypeText.setEnabled(true);
            jewellerySubTypeText.setText(getResources().getString(R.string.sub_type));
            ArrayAdapter<CharSequence> subList = ArrayAdapter.createFromResource(this, R.array.jewellery_er_sub_type, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
            jewellerySubTypeText.setAdapter(subList);
        } else if (subType.equals(mainList.get(3))) {
            jewellerySubTypeLayout.setEnabled(true);
            jewellerySubTypeText.setEnabled(true);
            jewellerySubTypeText.setText(getResources().getString(R.string.sub_type));
            ArrayAdapter<CharSequence> subList = ArrayAdapter.createFromResource(this, R.array.jewellery_br_sub_type, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
            jewellerySubTypeText.setAdapter(subList);
        } else {
            jewellerySubTypeText.setText(getResources().getString(R.string.sub_type));
            jewellerySubTypeLayout.setEnabled(false);
            jewellerySubTypeText.setEnabled(false);
        }
    }

    private String setInputText(float sliderValue, TextInputEditText editText, TextInputLayout layout) {
        float formComponent;
        if (!isChanging) {
            isChanging = true;
            layout.setErrorEnabled(false);
            formComponent = (float) (Math.ceil(sliderValue * 100) / 100);
            editText.setText(String.valueOf(formComponent));
            editText.setSelection(Objects.requireNonNull(editText.getText()).length());
            isChanging = false;
            return String.valueOf(formComponent);
        }
        return "0.00";
    }

    private String setSlider(TextInputEditText editText, TextInputLayout layout, Slider slider) {
        if (!isChanging) {
            isChanging = true;
            isError = false;
            String number = Objects.requireNonNull(editText.getText()).toString();
            if (number.isEmpty()) {
                slider.setValue(0.00f);
                isChanging = false;
                return "0.00";
            } else if (number.equals(".")) {
                number = "0.";
                editText.setText(number);
                editText.setSelection(editText.getText().length());
                isChanging = false;
                return "0.00";
            } else if (number.equals("0")) {
                number = "";
                editText.setText(number);
                editText.setSelection(editText.getText().length());
                isChanging = false;
                return "0.00";
            } else {
                if (number.matches("^(\\d+\\.\\d{1,2}|\\d+|\\d+\\.)$")) {
                    float sliderValue = Float.parseFloat(number);
                    if (sliderValue >= 0.00 && sliderValue <= 100.00) {
                        layout.setErrorEnabled(false);
                        slider.setValue(sliderValue);
                        editText.setSelection(editText.getText().length());
                        isChanging = false;

                        return String.valueOf(Math.ceil(sliderValue * 100) / 100);
                    } else {
                        layout.setErrorEnabled(true);
                        layout.setError(" ");
                        isError = true;
                        errorLayout = layout;
                        Toast.makeText(this,getResources().getString(R.string.slider_error1), Toast.LENGTH_SHORT).show();
                        slider.setValue(0.00f);
                        isChanging = false;
                        return "0.00";
                    }
                } else {
                    layout.setErrorEnabled(true);
                    layout.setError(" ");
                    isError = true;
                    errorLayout = layout;
                    Toast.makeText(this,getResources().getString(R.string.slider_error2), Toast.LENGTH_SHORT).show();
                    slider.setValue(0.00f);
                    isChanging = false;
                    return "0.00";
                }
            }
        }
        isChanging = false;
        return "0.00";
    }

    private void validateData() {
        designCode = Objects.requireNonNull(design_code_text.getText()).toString();
        mainType = jewelleryMainTypeText.getText().toString();
        subType = jewellerySubTypeText.getText().toString();
        customerCode = Objects.requireNonNull(customer_code_text.getText()).toString();
        tempCode = Objects.requireNonNull(temp_code_text.getText()).toString();
        workBy = Objects.requireNonNull(work_by_text.getText()).toString();
        workPlace = Objects.requireNonNull(work_place_text.getText()).toString();
        customerName = Objects.requireNonNull(customer_text.getText()).toString();
        selectedDate = Objects.requireNonNull(date_text.getText()).toString();
        status = designStatus.isChecked();

        design_code_layout.setErrorEnabled(false);
        customer_code_layout.setErrorEnabled(false);
        temp_code_layout.setErrorEnabled(false);
        jewelleryMainTypeLayout.setErrorEnabled(false);
        jewellerySubTypeLayout.setErrorEnabled(false);

        FormValidation fv = new FormValidation(designCode, customerCode, tempCode, mainType, subType, workBy, workPlace);
        fv.validate(result -> {
            switch (result) {
                case "Small Design Code": {
                    design_code_layout.setErrorEnabled(true);
                    design_code_layout.setError(" ");
                    design_code_text.setError(getResources().getString(R.string.small_design_code));
                    mainScrollView.scrollTo(0, design_code_layout.getTop());
                    break;
                }
                case "Small Customer Code": {
                    customer_code_layout.setErrorEnabled(true);
                    customer_code_layout.setError(" ");
                    customer_code_text.setError(getResources().getString(R.string.small_customer_code));
                    mainScrollView.scrollTo(0, customer_code_layout.getTop());
                    break;
                }
                case "Empty Temp Code": {
                    temp_code_layout.setErrorEnabled(true);
                    temp_code_layout.setError(" ");
                    temp_code_text.setError(getResources().getString(R.string.required_temp_code));
                    mainScrollView.scrollTo(0, temp_code_layout.getTop());
                    break;
                }
                case "Small Temp Code": {
                    temp_code_layout.setErrorEnabled(true);
                    temp_code_layout.setError(" ");
                    temp_code_text.setError(getResources().getString(R.string.small_temp_code));
                    mainScrollView.scrollTo(0, temp_code_layout.getTop());
                    break;
                }
                case "Empty Main Type": {
                    jewelleryMainTypeLayout.setErrorEnabled(true);
                    jewelleryMainTypeLayout.setError(" ");
                    mainScrollView.scrollTo(0, jewelleryMainTypeLayout.getTop());
                    break;
                }
                case "Empty sub Type": {
                    jewellerySubTypeLayout.setErrorEnabled(true);
                    jewellerySubTypeLayout.setError(" ");
                    mainScrollView.scrollTo(0, jewellerySubTypeLayout.getTop());
                    break;
                }
                case "work by empty": {
                    workBy = getResources().getString(R.string.work_by_text);
                    break;
                }
                case "work place empty": {
                    workPlace = getResources().getString(R.string.work_place_text);
                    break;
                }
                case "NO Error": {
                    isError = false;
                    errorLayout = null;
                    if (!designCode.isEmpty() && designCodeList.documentExits()) {
                        if (codes.isDesignCodeExists(designCode)) {
                            isError = true;
                            errorLayout = design_code_layout;
                            design_code_layout.setErrorEnabled(true);
                            design_code_layout.setError(" ");
                            design_code_text.setError(getResources().getString(R.string.exits_design_code));
                        }
                    }
                    if (!tempCode.isEmpty() && tempCodeList.documentExits()) {
                        if (codes.isTempCodeExits(tempCode)) {
                            isError = true;
                            errorLayout = temp_code_layout;
                            temp_code_layout.setErrorEnabled(true);
                            temp_code_layout.setError(" ");
                            temp_code_text.setError(getResources().getString(R.string.exits_temp_code));
                        }
                    }
                    if (isError) mainScrollView.scrollTo(0, errorLayout.getTop());
                    else {
                        changeList();
                    }
                }
            }
        });
    }

    private void changeList() {
        List<Object> newData = new ArrayList<>();
        newData.add(0,img1Bitmap == null ? "null" : img1Bitmap);
        newData.add(1,img2Bitmap == null ? "null" : img2Bitmap);
        newData.add(2,customerName);
        newData.add(3,designCode);
        newData.add(4,customerCode);
        newData.add(5,tempCode);
        newData.add(6,workBy);
        newData.add(7,workPlace);
        newData.add(8,selectedDate);
        newData.add(9,mainType);
        newData.add(10,subType);
        newData.add(11,String.valueOf(status));
        newData.add(12,length);
        newData.add(13,width);
        newData.add(14,height);
        newData.add(15,gold_weight);
        newData.add(16,diamond_weight);

        mainList.set(positionOfUpdate,newData);
        startActivity(new Intent(this, ExcelDataListActivity.class));
        finish();
    }
}