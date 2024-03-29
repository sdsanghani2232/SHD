package com.shd.ui.fragments.subFragments.addJewellery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.archit.calendardaterangepicker.customviews.CalendarListener;
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shd.R;
import com.shd.halperclass.informationclass.Codes;
import com.shd.repository.readdata.DesignCodeList;
import com.shd.repository.readdata.TempCodeList;
import com.shd.repository.storedata.JewelleryDetailsStore;
import com.shd.halperclass.otherClass.CheckInternet;
import com.shd.halperclass.validationclass.FormValidation;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FormFragment extends Fragment {
    private boolean isChanging = false, isInternet = true, isError = false, status = false;
    private String gold_weight = "0.0", diamond_weight = "0.0", length = "0.0", width = "0.0", height = "0.0", designCode, mainType, subType, customerCode, tempCode, customerName, workBy, workPlace, selectedDate;
    private final Codes codes = Codes.getInstance();
    private final TempCodeList tempCodeList = TempCodeList.getInstance();
    private final DesignCodeList designCodeList = DesignCodeList.getInstance();
    private final SimpleDateFormat format = new SimpleDateFormat("EEE, MMM dd yyyy", Locale.getDefault());
    ScrollView mainScrollView;
    View indicator1, indicator2;
    private Uri img1Url = Uri.parse(""), img2Url = Uri.parse("");
    ShapeableImageView jewelleryImg1, jewelleryImg2;
    MaterialSwitch designStatus;
    FloatingActionButton jewelleryImg1Button, jewelleryImg2Button;
    HorizontalScrollView imgScrollView;
    DateRangeCalendarView rangeCalendarView;
    TextInputLayout jewelleryMainTypeLayout, jewellerySubTypeLayout, gold_layout, diamond_layout, length_layout, width_layout, height_layout, design_code_layout, customer_code_layout, temp_code_layout, errorLayout;
    TextInputEditText gold_text, diamond_text, length_text, width_text, height_text, design_code_text, customer_text, work_by_text, work_place_text, customer_code_text, temp_code_text;
    MaterialAutoCompleteTextView jewelleryMainTypeText, jewellerySubTypeText;
    Slider gold_slider, diamond_slider, length_slider, width_slider, height_slider;
    Button save;

    public FormFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form, container, false);

        findIds(view);
        checkInternet();
        currentDate();
        setMainDropDownList();
        rangeCalendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onFirstDateSelected(@NonNull Calendar calendar) {}

            @Override
            public void onDateRangeSelected(@NonNull Calendar calendar, @NonNull Calendar calendar1) {
                selectedDate = format.format(calendar.getTime());
            }
        });
        jewelleryImg1Button.setOnClickListener(v -> img1.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)));
        jewelleryImg2Button.setOnClickListener(v -> img2.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)));
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

        save.setOnClickListener(v ->{
            checkInternet();
            if ((isInternet)) checkData();
            else {
                Snackbar.make(view.findViewById(R.id.form_page),R.string.internet_error,Snackbar.LENGTH_LONG)
                        .setBackgroundTint(requireContext().getColor(R.color.colorPrimaryStroke))
                        .setTextColor(requireContext().getColor(R.color.colorPrimaryText)).show();
            }
        });
        return view;
    }

    private void checkInternet() {
        isInternet = new CheckInternet(requireContext()).Check();
    }

    private void findIds(View view) {
        mainScrollView = view.findViewById(R.id.main_scroll_view);
        indicator1 = view.findViewById(R.id.form_img_indicator_1);
        indicator2 = view.findViewById(R.id.form_img_indicator_2);
        imgScrollView = view.findViewById(R.id.form_design_horizontal_scroll_bar);
        rangeCalendarView = view.findViewById(R.id.form_calender);
        jewelleryMainTypeLayout = view.findViewById(R.id.form_jewellery_main_type_layout);
        jewelleryMainTypeText = view.findViewById(R.id.form_jewellery_main_type_drop_down);
        jewellerySubTypeLayout = view.findViewById(R.id.form_jewellery_sub_type_layout);
        jewellerySubTypeText = view.findViewById(R.id.form_jewellery_sub_type_drop_down);
        gold_slider = view.findViewById(R.id.form_gold_slider);
        gold_text = view.findViewById(R.id.form_gold_input_text);
        gold_layout = view.findViewById(R.id.form_gold_text_layout);
        diamond_layout = view.findViewById(R.id.form_diamond_text_layout);
        diamond_text = view.findViewById(R.id.form_diamond_input_text);
        diamond_slider = view.findViewById(R.id.form_diamond_slider);
        length_layout = view.findViewById(R.id.form_length_text_layout);
        length_text = view.findViewById(R.id.form_length_input_text);
        length_slider = view.findViewById(R.id.form_length_slider);
        width_layout = view.findViewById(R.id.form_width_text_layout);
        width_text = view.findViewById(R.id.form_width_input_text);
        width_slider = view.findViewById(R.id.form_width_slider);
        height_layout = view.findViewById(R.id.form_height_text_layout);
        height_text = view.findViewById(R.id.form_height_input_text);
        height_slider = view.findViewById(R.id.form_height_slider);
        save = view.findViewById(R.id.form_save_button);
        design_code_layout = view.findViewById(R.id.design_code_layout);
        design_code_text = view.findViewById(R.id.design_code_text);
        jewelleryImg1Button = view.findViewById(R.id.form_design_img_pencil_1);
        jewelleryImg2Button = view.findViewById(R.id.form_design_img_pencil_2);
        jewelleryImg1 = view.findViewById(R.id.form_design_img_1);
        jewelleryImg2 = view.findViewById(R.id.form_design_img_2);
        customer_text = view.findViewById(R.id.form_customer_input_text);
        work_by_text = view.findViewById(R.id.form_work_by_input_text);
        work_place_text = view.findViewById(R.id.form_work_place_input_text);
        designStatus = view.findViewById(R.id.form_status_switch);
        customer_code_layout = view.findViewById(R.id.form_customer_code_layout);
        customer_code_text = view.findViewById(R.id.form_customer_code_input_text);
        temp_code_layout = view.findViewById(R.id.form_temp_code_layout);
        temp_code_text = view.findViewById(R.id.form_temp_code_input_text);
    }

    public final ActivityResultLauncher<Intent> img1 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK || result.getData() != null) {
                assert result.getData() != null;
                img1Url = result.getData().getData();
                jewelleryImg1.setImageURI(img1Url);
            }
        }
    });
    public final ActivityResultLauncher<Intent> img2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK || result.getData() != null) {
                assert result.getData() != null;
                img2Url = result.getData().getData();
                jewelleryImg2.setImageURI(img2Url);
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

    private void currentDate() {
//        set current date in data base
        Calendar calendars = Calendar.getInstance();
        selectedDate = format.format(calendars.getTime());

//        set current date in view
        Calendar currentMonth = Calendar.getInstance();
        currentMonth.add(Calendar.MONTH,0);
        Calendar currentDate = (Calendar) currentMonth.clone();
        currentDate.add(Calendar.DATE, 0);

        rangeCalendarView.setSelectedDateRange(currentMonth, currentDate);
    }

    private void setMainDropDownList() {
        ArrayAdapter<CharSequence> mainList = ArrayAdapter.createFromResource(requireContext(), R.array.jewellery_main_type, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        jewelleryMainTypeText.setAdapter(mainList);
        jewellerySubTypeLayout.setEnabled(false);
        jewellerySubTypeText.setEnabled(false);
    }

    private void setSubDropDown(String subType) {
        List<String> mainList = Arrays.asList(getResources().getStringArray(R.array.jewellery_main_type));

        if (subType.equals(mainList.get(0))) {
            jewellerySubTypeLayout.setEnabled(true);
            jewellerySubTypeText.setEnabled(true);
            jewellerySubTypeText.setText(requireContext().getResources().getString(R.string.sub_type));
            ArrayAdapter<CharSequence> subList = ArrayAdapter.createFromResource(requireContext(), R.array.jewellery_ring_sub_type, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
            jewellerySubTypeText.setAdapter(subList);
        } else if (subType.equals(mainList.get(1))) {
            jewellerySubTypeLayout.setEnabled(true);
            jewellerySubTypeText.setEnabled(true);
            jewellerySubTypeText.setText(requireContext().getResources().getString(R.string.sub_type));
            ArrayAdapter<CharSequence> subList = ArrayAdapter.createFromResource(requireContext(), R.array.jewellery_nkc_sub_type, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
            jewellerySubTypeText.setAdapter(subList);
        } else if (subType.equals(mainList.get(2))) {
            jewellerySubTypeLayout.setEnabled(true);
            jewellerySubTypeText.setEnabled(true);
            jewellerySubTypeText.setText(requireContext().getResources().getString(R.string.sub_type));
            ArrayAdapter<CharSequence> subList = ArrayAdapter.createFromResource(requireContext(), R.array.jewellery_er_sub_type, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
            jewellerySubTypeText.setAdapter(subList);
        } else if (subType.equals(mainList.get(3))) {
            jewellerySubTypeLayout.setEnabled(true);
            jewellerySubTypeText.setEnabled(true);
            jewellerySubTypeText.setText(requireContext().getResources().getString(R.string.sub_type));
            ArrayAdapter<CharSequence> subList = ArrayAdapter.createFromResource(requireContext(), R.array.jewellery_br_sub_type, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
            jewellerySubTypeText.setAdapter(subList);
        } else {
            jewellerySubTypeText.setText(requireContext().getResources().getString(R.string.sub_type));
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
                editText.setHint("0.0");
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
                        Toast.makeText(requireContext(), requireContext().getResources().getString(R.string.slider_error1), Toast.LENGTH_SHORT).show();
                        slider.setValue(0.00f);
                        isChanging = false;
                        return "0.00";
                    }
                } else {
                    layout.setErrorEnabled(true);
                    layout.setError(" ");
                    isError = true;
                    errorLayout = layout;
                    Toast.makeText(requireContext(), requireContext().getResources().getString(R.string.slider_error2), Toast.LENGTH_SHORT).show();
                    slider.setValue(0.00f);
                    isChanging = false;
                    return "0.00";
                }
            }
        }
        isChanging = false;
        return "0.00";
    }

    private void checkData() {

        designCode = Objects.requireNonNull(design_code_text.getText()).toString();
        mainType = jewelleryMainTypeText.getText().toString();
        subType = jewellerySubTypeText.getText().toString();
        customerCode = Objects.requireNonNull(customer_code_text.getText()).toString();
        tempCode = Objects.requireNonNull(temp_code_text.getText()).toString();
        workBy = Objects.requireNonNull(work_by_text.getText()).toString();
        workPlace = Objects.requireNonNull(work_place_text.getText()).toString();
        customerName = Objects.requireNonNull(customer_text.getText()).toString();
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
                    design_code_layout.setError("");
                    design_code_text.setError(requireContext().getResources().getString(R.string.small_design_code));
                    mainScrollView.scrollTo(0, design_code_layout.getTop());
                    break;
                }
                case "Small Customer Code": {
                    customer_code_layout.setErrorEnabled(true);
                    customer_code_layout.setError("");
                    customer_code_text.setError(requireContext().getResources().getString(R.string.small_customer_code));
                    mainScrollView.scrollTo(0, customer_code_layout.getTop());
                    break;
                }
                case "Empty Temp Code": {
                    temp_code_layout.setErrorEnabled(true);
                    temp_code_layout.setError(" ");
                    temp_code_text.setError(requireContext().getResources().getString(R.string.required_temp_code));
                    mainScrollView.scrollTo(0, temp_code_layout.getTop());
                    break;
                }
                case "Small Temp Code": {
                    temp_code_layout.setErrorEnabled(true);
                    temp_code_layout.setError(" ");
                    temp_code_text.setError(requireContext().getResources().getString(R.string.small_temp_code));
                    mainScrollView.scrollTo(0, temp_code_layout.getTop());
                    break;
                }
                case "Empty Main Type": {
                    jewelleryMainTypeLayout.setErrorEnabled(true);
                    jewelleryMainTypeLayout.setError("");
                    mainScrollView.scrollTo(0, jewelleryMainTypeLayout.getTop());
                    break;
                }
                case "Empty sub Type": {
                    jewellerySubTypeLayout.setErrorEnabled(true);
                    jewellerySubTypeLayout.setError("");
                    mainScrollView.scrollTo(0, jewellerySubTypeLayout.getTop());
                    break;
                }
                case "work by empty": {
                    workBy = requireContext().getResources().getString(R.string.work_by_text);
                    break;
                }
                case "work place empty": {
                    workPlace = requireContext().getResources().getString(R.string.work_place_text);
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
                            design_code_layout.setError("");
                            design_code_text.setError(requireContext().getResources().getString(R.string.exits_design_code));
                        }
                    }
                    if (!tempCode.isEmpty() && tempCodeList.documentExits()) {
                        if (codes.isTempCodeExits(tempCode)) {
                            isError = true;
                            errorLayout = temp_code_layout;
                            temp_code_layout.setErrorEnabled(true);
                            temp_code_layout.setError(" ");
                            temp_code_text.setError(requireContext().getResources().getString(R.string.exits_temp_code));
                        }
                    }
                    if (isError) mainScrollView.scrollTo(0, errorLayout.getTop());
                    else {
                        storeData();
                        resetForm();
                    }
                }
            }
        });
    }

    private void storeData() {

        LayoutInflater inflater2 = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogBoxView = inflater2.inflate(R.layout.custom_dialog_box, null);
        Button dialogCancel = dialogBoxView.findViewById(R.id.cancel_button);
        LottieAnimationView animationView = dialogBoxView.findViewById(R.id.lottie_animation_view);
        TextView dialogMsg = dialogBoxView.findViewById(R.id.description);
        dialogCancel.setEnabled(false);
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireContext(), R.style.Theme_SHD_dialogBox)
                .setTitle(requireContext().getResources().getString(R.string.processing))
                .setCancelable(false);

        dialogBuilder.setView(dialogBoxView);
        Handler handler = new Handler(Looper.getMainLooper());
        animationView.setAnimation("spinner_circle.json");
        AlertDialog dialog = dialogBuilder.create();

        JewelleryDetailsStore jds = new JewelleryDetailsStore(img1Url, img2Url, customerName, designCode, status, customerCode, tempCode, workBy, workPlace, selectedDate, mainType, subType, length, width, height, gold_weight, diamond_weight);
        jds.storeFormJewelleryImg(result -> requireActivity().runOnUiThread(() -> {
            switch (result) {
                case "Img Not Stored": {
                    dialogCancel.setEnabled(true);
                    dialog.setTitle(requireContext().getResources().getString(R.string.warning));
                    dialogMsg.setTextColor(requireContext().getColor(R.color.colorPrimaryError));
                    dialogMsg.setText(requireContext().getResources().getString(R.string.Design_Img_Not_Stored));
                    animationView.setAnimation("warning.json");
                    break;
                }
                case "Design Not Store": {
                    dialogCancel.setEnabled(true);
                    dialog.setTitle(requireContext().getResources().getString(R.string.warning));
                    dialogMsg.setTextColor(requireContext().getColor(R.color.colorPrimaryError));
                    dialogMsg.setText(requireContext().getResources().getString(R.string.Design_Not_Stored));
                    animationView.setAnimation("warning.json");
                    break;
                }
                case "Successfully": {
                    dialogCancel.setEnabled(true);
                    dialog.setTitle(requireContext().getResources().getString(R.string.successfully));
                    dialogMsg.setTextColor(requireContext().getColor(R.color.colorPrimaryButton));
                    dialogMsg.setText(requireContext().getResources().getString(R.string.Design_Stored_Successfully));
                    animationView.setAnimation("complete.json");
                    break;
                }
            }
        }));

        Runnable animationRunnable = new Runnable() {
            @Override
            public void run() {
                animationView.playAnimation();
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(animationRunnable, 200);
        dialog.show();

        dialogCancel.setOnClickListener(v -> {
            handler.removeCallbacks(animationRunnable);
            animationView.cancelAnimation();
            dialog.dismiss();
        });
    }


    private void resetForm() {
        jewelleryImg1.setImageResource(R.drawable.default_img);
        jewelleryImg2.setImageResource(R.drawable.default_img);
        currentDate();
        designStatus.setChecked(false);
        jewelleryMainTypeText.setText(requireContext().getResources().getString(R.string.select_jewellery));
        jewellerySubTypeText.setText(requireContext().getResources().getString(R.string.select_sub_type));
        jewellerySubTypeLayout.setEnabled(false);
        setMainDropDownList();
        gold_slider.setValue(0.00f);
        diamond_slider.setValue(0.00f);
        length_slider.setValue(0.00f);
        width_slider.setValue(0.00f);
        height_slider.setValue(0.00f);
        gold_text.setText(requireContext().getResources().getString(R.string.form_starting_number));
        diamond_text.setText(requireContext().getResources().getString(R.string.form_starting_number));
        length_text.setText(requireContext().getResources().getString(R.string.form_starting_number));
        width_text.setText(requireContext().getResources().getString(R.string.form_starting_number));
        height_text.setText(requireContext().getResources().getString(R.string.form_starting_number));
        design_code_text.setText("");
        customer_text.setText("");
        work_by_text.setText(requireContext().getResources().getString(R.string.work_by_text));
        work_place_text.setText(requireContext().getResources().getString(R.string.work_place_text));
        customer_code_text.setText("");
        temp_code_text.setText("");
    }
}