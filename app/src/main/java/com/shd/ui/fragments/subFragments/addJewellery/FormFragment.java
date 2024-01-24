package com.shd.ui.fragments.subFragments.addJewellery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
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
    private boolean isChanging = false,isInternet = true,isError = false,status = false;
    private String gold_weight = "0.0",diamond_weight="0.0",length="0.0",width="0.0",height="0.0",designCode,mainType,subType,customerCode,tempCode,customerName,workBy,workPlace,selectedDate;
    private final Codes codes = Codes.getInstance();
    private final TempCodeList tempCodeList = TempCodeList.getInstance();
    private final DesignCodeList designCodeList = DesignCodeList.getInstance();
    ScrollView mainScrollView;
    TextView errorText;
    View indicator1,indicator2;
    private Uri img1Url = Uri.parse(""),img2Url= Uri.parse("");
    ShapeableImageView jewelleryImg1,jewelleryImg2;
    MaterialSwitch designStatus;
    FloatingActionButton jewelleryImg1Button,jewelleryImg2Button;
    HorizontalScrollView imgScrollView;
    TextInputLayout date_layout,jewelleryMainTypeLayout,jewellerySubTypeLayout,gold_layout,diamond_layout,length_layout,width_layout,height_layout,design_code_layout,customer_code_layout,temp_code_layout,errorLayout;
    TextInputEditText date_text,gold_text,diamond_text,length_text,width_text,height_text,design_code_text,customer_text,work_by_text,work_place_text,customer_code_text,temp_code_text;
    MaterialAutoCompleteTextView jewelleryMainTypeText,jewellerySubTypeText;
    Slider gold_slider,diamond_slider,length_slider,width_slider,height_slider;
    Button save;
    public FormFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @SuppressLint({"ClickableViewAccessibility"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form, container, false);

        findIds(view);
        checkInternet();
        currentDate();
        setMainDropDownList();
        jewelleryImg1Button.setOnClickListener(v -> img1.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)));
        jewelleryImg2Button.setOnClickListener(v -> img2.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)));
        date_layout.setStartIconOnClickListener(v ->setDate());
        date_text.setOnClickListener(v -> setDate());
        imgScrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> setIndicator(scrollX));
        jewelleryMainTypeText.setOnItemClickListener((parent, view1, position, id) -> {
            jewelleryMainTypeLayout.setErrorEnabled(false);
            setSubDropDown(parent.getItemAtPosition(position).toString());
        });
        gold_slider.addOnChangeListener((slider, value, fromUser) -> gold_weight = setInputText(value,gold_text,gold_layout));
        diamond_slider.addOnChangeListener((slider, value, fromUser) -> diamond_weight = setInputText(value,diamond_text,diamond_layout));
        length_slider.addOnChangeListener((slider, value, fromUser) -> length = setInputText(value,length_text,length_layout));
        width_slider.addOnChangeListener((slider, value, fromUser) -> width = setInputText(value,width_text,width_layout));
        height_slider.addOnChangeListener((slider, value, fromUser) -> height = setInputText(value,height_text,height_layout));

        gold_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {gold_text.setSelection(Objects.requireNonNull(gold_text.getText()).length());}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                gold_weight = setSlider(gold_text,gold_layout,gold_slider);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        diamond_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {diamond_text.setSelection(Objects.requireNonNull(diamond_text.getText()).length());}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { diamond_weight = setSlider(diamond_text,diamond_layout,diamond_slider); }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        length_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {length_text.setSelection(Objects.requireNonNull(length_text.getText()).length());}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { length = setSlider(length_text,length_layout,length_slider);}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        width_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {width_text.setSelection(Objects.requireNonNull(width_text.getText()).length());}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { width = setSlider(width_text,width_layout,width_slider);}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        height_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {height_text.setSelection(Objects.requireNonNull(height_text.getText()).length());}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { height = setSlider(height_text,height_layout,height_slider);}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        save.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    checkInternet();
                    if(isInternet)
                    {
                        checkData();
                        save.setBackgroundColor(requireContext().getColor(R.color.form_button_background_onClick_color));
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if(isInternet)
                    {
                        save.setBackgroundColor(requireContext().getColor(R.color.form_button_background_color));
                    }else {
                        save.setBackgroundColor(requireContext().getColor(R.color.form_not_enable_button_color));
                    }
                    break;
            }
            return true;
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void checkInternet() {
        if(new CheckInternet(requireContext()).Check())
        {
            isInternet = true;
            save.setClickable(true);
            save.setBackgroundColor(requireContext().getColor(R.color.form_button_background_color));
            errorText.setVisibility(View.GONE);
        }else {
            isInternet = false;
            save.setClickable(false);
            save.setBackgroundColor(requireContext().getColor(R.color.form_not_enable_button_color));
            errorText.setText("Check Internet Connectivity");
            errorText.setVisibility(View.VISIBLE);
            mainScrollView.scrollTo(0,0);
        }
    }

    private void findIds(View view) {
        mainScrollView = view.findViewById(R.id.main_scroll_view);
        errorText = view.findViewById(R.id.error_text);
        indicator1 = view.findViewById(R.id.form_img_indicator_1);
        indicator2 = view.findViewById(R.id.form_img_indicator_2);
        imgScrollView = view.findViewById(R.id.form_design_horizontal_scroll_bar);
        date_layout = view.findViewById(R.id.form_date_layout);
        date_text = view.findViewById(R.id.form_date_display_text);
        jewelleryMainTypeLayout = view.findViewById(R.id.form_jewellery_main_type_layout);
        jewelleryMainTypeText = view.findViewById(R.id.form_jewellery_main_type_drop_down);
        jewellerySubTypeLayout = view.findViewById(R.id.form_jewellery_sub_type_layout);
        jewellerySubTypeText = view.findViewById(R.id.form_jewellery_sub_type_drop_down);
        gold_slider = view.findViewById(R.id.form_gold_slider);
        gold_text= view.findViewById(R.id.form_gold_input_text);
        gold_layout = view.findViewById(R.id.form_gold_text_layout);
        diamond_layout = view.findViewById(R.id.form_diamond_text_layout);
        diamond_text = view.findViewById(R.id.form_diamond_input_text);
        diamond_slider = view.findViewById(R.id.form_diamond_slider);
        length_layout = view.findViewById(R.id.form_length_text_layout);
        length_text= view.findViewById(R.id.form_length_input_text);
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

    public ActivityResultLauncher<Intent> img1 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK || result.getData() != null)
            {
                assert result.getData() != null;
                img1Url = result.getData().getData();
                jewelleryImg1.setImageURI(img1Url);
            }
        }
    });
    public ActivityResultLauncher<Intent> img2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == Activity.RESULT_OK || result.getData() != null)
            {
                assert result.getData() != null;
                img2Url = result.getData().getData();
                jewelleryImg2.setImageURI(img2Url);
            }
        }
    });

    private void setIndicator(int scrollX)
    {
        int maxScrollX = imgScrollView.getChildAt(0).getWidth() - imgScrollView.getWidth();
        float scroll = (float) scrollX / maxScrollX * 100;

        if(scroll < 50 )
        {
            indicator1.setBackgroundResource(R.drawable.img_scroll_indicator_selected);
            indicator2.setBackgroundResource(R.drawable.img_scroll_indicator_not_selected);
        }else {
            indicator1.setBackgroundResource(R.drawable.img_scroll_indicator_not_selected);
            indicator2.setBackgroundResource(R.drawable.img_scroll_indicator_selected);
        }
    }

    private void currentDate()
    {
        Calendar calendars = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM dd yyyy", Locale.getDefault());
        date_text.setText(format.format(calendars.getTime()));
    }
    private void setDate()
    {
        CalendarConstraints.Builder builder = new CalendarConstraints.Builder();
        builder.setValidator(DateValidatorPointBackward.now());

        MaterialDatePicker.Builder<Long> dateBuilder = MaterialDatePicker.Builder.datePicker();
        dateBuilder.setTheme(R.style.Theme_SHD_form_calender);
        dateBuilder.setCalendarConstraints(builder.build());

        long today = MaterialDatePicker.todayInUtcMilliseconds();
        dateBuilder.setSelection(today);

        MaterialDatePicker<Long> datePicker = dateBuilder.build();

        dateBuilder.setTitleText("Select Date");
        dateBuilder.setSelection(System.currentTimeMillis());

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendars = Calendar.getInstance();
            calendars.setTimeInMillis(selection);
            SimpleDateFormat format = new SimpleDateFormat("EEE, MMM dd yyyy", Locale.getDefault());
            date_text.setText(format.format(calendars.getTime()));
        });

        datePicker.show(requireActivity().getSupportFragmentManager(), "Jewellery Date");
    }

    private void setMainDropDownList() {
        ArrayAdapter<CharSequence> mainList = ArrayAdapter.createFromResource(requireContext(),R.array.jewellery_main_type, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        jewelleryMainTypeText.setAdapter(mainList);
        jewellerySubTypeLayout.setEnabled(false);
        jewellerySubTypeText.setEnabled(false);
    }

    @SuppressLint("SetTextI18n")
    private void setSubDropDown(String subType) {
        List<String> mainList = Arrays.asList(getResources().getStringArray(R.array.jewellery_main_type));

        if(subType.equals(mainList.get(0)))
        {
            jewellerySubTypeLayout.setEnabled(true);
            jewellerySubTypeText.setEnabled(true);
            jewellerySubTypeText.setText("Sub Type");
            ArrayAdapter<CharSequence> subList = ArrayAdapter.createFromResource(requireContext(),R.array.jewellery_ring_sub_type, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
            jewellerySubTypeText.setAdapter(subList);
        } else if (subType.equals(mainList.get(1))) {
            jewellerySubTypeLayout.setEnabled(true);
            jewellerySubTypeText.setEnabled(true);
            jewellerySubTypeText.setText("Sub Type");
            ArrayAdapter<CharSequence> subList = ArrayAdapter.createFromResource(requireContext(),R.array.jewellery_nkc_sub_type, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
            jewellerySubTypeText.setAdapter(subList);
        } else if (subType.equals(mainList.get(2))) {
            jewellerySubTypeLayout.setEnabled(true);
            jewellerySubTypeText.setEnabled(true);
            jewellerySubTypeText.setText("Sub Type");
            ArrayAdapter<CharSequence> subList = ArrayAdapter.createFromResource(requireContext(),R.array.jewellery_er_sub_type, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
            jewellerySubTypeText.setAdapter(subList);
        } else if (subType.equals(mainList.get(3))) {
            jewellerySubTypeLayout.setEnabled(true);
            jewellerySubTypeText.setEnabled(true);
            jewellerySubTypeText.setText("Sub Type");
            ArrayAdapter<CharSequence> subList = ArrayAdapter.createFromResource(requireContext(),R.array.jewellery_br_sub_type, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
            jewellerySubTypeText.setAdapter(subList);
        } else {
            jewellerySubTypeText.setText("No Sub Type");
            jewellerySubTypeLayout.setEnabled(false);
            jewellerySubTypeText.setEnabled(false);
        }
    }

    private String setInputText(float sliderValue , TextInputEditText editText ,TextInputLayout layout)
    {
        float formComponent;
        if(!isChanging)
        {
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

    private String setSlider(TextInputEditText editText ,TextInputLayout layout,Slider slider)
    {
        if(!isChanging)
        {
            isChanging = true;
            isError = false;
            String number = Objects.requireNonNull(editText.getText()).toString();
            if(number.isEmpty()) {
                slider.setValue(0.00f);
                isChanging = false;
                return "0.00";
            }
            else if (number.equals(".")) {
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
                if(number.matches("^(\\d+\\.\\d{1,2}|\\d+|\\d+\\.)$"))
                {
                    float sliderValue = Float.parseFloat(number);
                    if(sliderValue >= 0.00 && sliderValue <= 100.00)
                    {
                        layout.setErrorEnabled(false);
                        slider.setValue(sliderValue);
                        editText.setSelection(editText.getText().length());
                        isChanging = false;

                        return String.valueOf(Math.ceil(sliderValue * 100) / 100);
                    }
                    else {
                        layout.setErrorEnabled(true);
                        layout.setError(" ");
//                            TODO : make custom Toast
                        isError = true;
                        errorLayout = layout;
                        Toast.makeText(requireContext(), "Only 0.00 to 100.00 number Allow", Toast.LENGTH_SHORT).show();
                        slider.setValue(0.00f);
                        isChanging = false;
                        return "0.00";
                    }
                }else {
                    layout.setErrorEnabled(true);
                    layout.setError(" ");
                    isError = true;
                    errorLayout = layout;
                    Toast.makeText(requireContext(), "Only 2 Decimal Allow", Toast.LENGTH_SHORT).show();
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
        selectedDate = Objects.requireNonNull(date_text.getText()).toString();
        status = designStatus.isChecked();

        design_code_layout.setErrorEnabled(false);
        customer_code_layout.setErrorEnabled(false);
        temp_code_layout.setErrorEnabled(false);
        jewelleryMainTypeLayout.setErrorEnabled(false);
        jewellerySubTypeLayout.setErrorEnabled(false);

        FormValidation fv = new FormValidation(designCode,customerCode,tempCode,mainType,subType,workBy,workPlace);
        fv.validate(result -> {
            switch (result){
                case "Small Design Code" :
                {
                    design_code_layout.setErrorEnabled(true);
                    design_code_layout.setError(" ");
                    design_code_text.setError("Enter minimum 5 charter Design Code ");
                    mainScrollView.scrollTo(0,design_code_layout.getTop());
                    break;
                }
                case "Small Customer Code" :
                {
                    customer_code_layout.setErrorEnabled(true);
                    customer_code_layout.setError(" ");
                    customer_code_text.setError("Enter minimum 5 charter Customer Code");
                    mainScrollView.scrollTo(0,customer_code_layout.getTop());
                    break;
                }
                case "Empty Temp Code" :
                {
                    temp_code_layout.setErrorEnabled(true);
                    temp_code_layout.setError(" ");
                    temp_code_text.setError("Enter Temp Code Required");
                    mainScrollView.scrollTo(0,temp_code_layout.getTop());
                    break;
                }
                case "Small Temp Code" :
                {
                    temp_code_layout.setErrorEnabled(true);
                    temp_code_layout.setError(" ");
                    temp_code_text.setError("Enter minimum 5 charter Temp Code");
                    mainScrollView.scrollTo(0,temp_code_layout.getTop());
                    break;
                }
                case "Empty Main Type" :
                {
                    jewelleryMainTypeLayout.setErrorEnabled(true);
                    jewelleryMainTypeLayout.setError(" ");
                    mainScrollView.scrollTo(0,jewelleryMainTypeLayout.getTop());
                    break;
                }
                case "Empty sub Type" :
                {
                    jewellerySubTypeLayout.setErrorEnabled(true);
                    jewellerySubTypeLayout.setError(" ");
                    mainScrollView.scrollTo(0,jewellerySubTypeLayout.getTop());
                    break;
                }
                case "work by empty" :
                {
                    workBy = "SHD";
                    break;
                }
                case "work place empty" :
                {
                    workPlace = "SHD office";
                    break;
                }
                case "NO Error" :
                {
                    if (!designCode.isEmpty() && designCodeList.documentExits()) {
                        if(codes.isDesignCodeExists(designCode))
                        {
                            design_code_layout.setErrorEnabled(true);
                            design_code_layout.setError(" ");
                            design_code_text.setError("Design Code already exits");
                            mainScrollView.scrollTo(0, design_code_layout.getTop());
                        }
                    }
                    if (!tempCode.isEmpty() && tempCodeList.documentExits()) {
                        if(codes.isTempCodeExits(tempCode))
                        {
                            temp_code_layout.setErrorEnabled(true);
                            temp_code_layout.setError(" ");
                            temp_code_text.setError("Temp Code already exits");
                            mainScrollView.scrollTo(0,temp_code_layout.getTop());
                        }
                    }
                    if(isError) mainScrollView.scrollTo(0,errorLayout.getTop());
                    else {
                        errorText.setVisibility(View.GONE);
                        storeData();
                    }
                }
            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void storeData() {
        Log.d("values","img 1 :"+img1Url+"\nimg 2 :"+img2Url+"\nCustomer Name :"+customerName+"\ncustomer code :"+customerCode+
                "\ndesign code :"+designCode+"\ntemp code :"+tempCode+"\nstatus :"+status+"\nwork by :"+workBy+"\nwork place :"+workPlace+
                "\ndate :"+selectedDate+"\nmain type :"+mainType+"\nsub type :"+subType+"\ngold :"+gold_weight+"\ndiamond :"+diamond_weight+
                "\nlength :"+length+"\nwidth :"+width+"\nheight :"+height);

        JewelleryDetailsStore jds = new JewelleryDetailsStore(img1Url,img2Url,customerName,designCode,status,customerCode,tempCode,workBy,workPlace,selectedDate,mainType,subType,length,width,height,gold_weight,diamond_weight);
        jds.StoreJewelleryImg(result -> {
            switch (result)
            {
                case "Img Not Store" :
                {
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText("Img Not Stored");
                    mainScrollView.scrollTo(0,0);
                    break;
                }
                case "Design Not Store" :
                {
                    errorText.setVisibility(View.VISIBLE);
                    errorText.setText("Design Not Stored");
                    mainScrollView.scrollTo(0,0);
                    break;
                }
            }
        });
    }

}