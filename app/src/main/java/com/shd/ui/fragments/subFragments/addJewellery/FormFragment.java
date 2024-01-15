package com.shd.ui.fragments.subFragments.addJewellery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.Toast;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shd.R;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FormFragment extends Fragment {
    private boolean isChanging = false;
    View indicator1,indicator2;
    HorizontalScrollView imgScrollView;
    TextInputLayout date_layout,jewelleryMainTypeLayout,jewellerySubTypeLayout,gold_layout,diamond_layout,length_layout,width_layout,height_layout;
    TextInputEditText date_text,gold_text,diamond_text,length_text,width_text,height_text;
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

        currentDate();
        setMainDropDownList();
        date_layout.setStartIconOnClickListener(v ->setDate());

        date_text.setOnClickListener(v -> setDate());

        imgScrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> setIndicator(scrollX));
        jewelleryMainTypeText.setOnItemClickListener((parent, view1, position, id) -> setSubDropDown(parent.getItemAtPosition(position).toString()));
        gold_slider.addOnChangeListener((slider, value, fromUser) -> setGoldInputText(value));
        diamond_slider.addOnChangeListener((slider, value, fromUser) -> setDiamondInputText(value));
        length_slider.addOnChangeListener((slider, value, fromUser) -> setLengthInputText(value));
        width_slider.addOnChangeListener((slider, value, fromUser) -> setWidthInputText(value));
        height_slider.addOnChangeListener((slider, value, fromUser) -> setHeightInputText(value));
        gold_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { setGoldSlider(); }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        diamond_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { setDiamondSlider(); }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        length_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { setLengthSlider();}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        width_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { setWidthSlider();}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        height_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { setHeightSlider();}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        save.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    checkData();
                    break;
                case MotionEvent.ACTION_UP:
                    save.setBackgroundColor(requireContext().getColor(R.color.form_button_background_color));
                    break;
            }
            return true;
        });

        return view;
    }

    private void findIds(View view) {
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
    }


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
        dateBuilder.setTheme(R.style.Theme_SHD_from_calender);
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
            ArrayAdapter<CharSequence> subList = ArrayAdapter.createFromResource(requireContext(),R.array.jewellery_necklace_sub_type, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
            jewellerySubTypeText.setAdapter(subList);
        } else if (subType.equals(mainList.get(2))) {
            jewellerySubTypeLayout.setEnabled(true);
            jewellerySubTypeText.setEnabled(true);
            jewellerySubTypeText.setText("Sub Type");
            ArrayAdapter<CharSequence> subList = ArrayAdapter.createFromResource(requireContext(),R.array.jewellery_er_sub_type, androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
            jewellerySubTypeText.setAdapter(subList);
        }else {
            jewellerySubTypeText.setText("No Sub Type");
            jewellerySubTypeLayout.setEnabled(false);
            jewellerySubTypeText.setEnabled(false);
        }
    }

    @SuppressLint("DefaultLocale")
    private void setGoldInputText(float value) {
        if(!isChanging) {
            isChanging = true;
            gold_text.setText(String.format("%.2f", value));
            gold_text.setSelection(Objects.requireNonNull(gold_text.getText()).length());
            isChanging = false;
        }
    }

    private void setGoldSlider() {
        if(!isChanging)
        {
            isChanging = true;
            String number = Objects.requireNonNull(gold_text.getText()).toString();
            if(number.isEmpty())
            {
                gold_slider.setValue(0.00f);
            }
            if(number.equals("."))
            {
                number="0.0";
                gold_text.setText("0.");
                gold_text.setSelection(gold_text.getText().length());
            }
            if(!number.isEmpty())
            {
                if (number.matches("^(\\d+\\.\\d{1,2}|\\d+|\\d+\\.)$"))
                {
                    float sliderValue = Float.parseFloat(number);
                    if(sliderValue >= 0.00 && sliderValue <= 100.00)
                    {
                        gold_layout.setErrorEnabled(false);
                        gold_slider.setValue(sliderValue);
                        gold_text.setSelection(gold_text.getText().length());
                    }
                   else {
                        gold_layout.setErrorEnabled(true);
                        gold_layout.setError(" ");
//                            TODO : make custom Toast
                        Toast.makeText(requireContext(), "Only 0.00 to 100.00 number Allow", Toast.LENGTH_SHORT).show();
                        gold_slider.setValue(0.00f);
                    }
                }else {
                    gold_layout.setErrorEnabled(true);
                    gold_layout.setError(" ");
                    Toast.makeText(requireContext(), "Only 2 Decimal Allow", Toast.LENGTH_SHORT).show();
                    gold_slider.setValue(0.00f);
                }
            }
            isChanging = false;
        }
    }

    @SuppressLint("DefaultLocale")
    private void setDiamondInputText(float value) {
        if(!isChanging) {
            isChanging = true;
            diamond_text.setText(String.format("%.2f", value));
            diamond_text.setSelection(Objects.requireNonNull(diamond_text.getText()).length());
            isChanging = false;
        }
    }

    private void setDiamondSlider() {
        if(!isChanging)
        {
            isChanging = true;
            String number = Objects.requireNonNull(diamond_text.getText()).toString();
            if(number.isEmpty())
            {
                diamond_slider.setValue(0.00f);
            }
            if(number.equals("."))
            {
                number="0.0";
                diamond_text.setText("0.");
                diamond_text.setSelection(diamond_text.getText().length());
            }
            if(!number.isEmpty())
            {
                if (number.matches("^(\\d+\\.\\d{1,2}|\\d+|\\d+\\.)$"))
                {
                    float sliderValue = Float.parseFloat(number);
                    if(sliderValue >= 0.00 && sliderValue <= 100.00)
                    {
                        diamond_layout.setErrorEnabled(false);
                        diamond_slider.setValue(sliderValue);
                        diamond_text.setSelection(diamond_text.getText().length());
                    }
                    else {
                        diamond_layout.setErrorEnabled(true);
                        diamond_layout.setError(" ");
                        Toast.makeText(requireContext(), "Only 0.00 to 100.00 number Allow", Toast.LENGTH_SHORT).show();
                        diamond_slider.setValue(0.00f);
                    }
                }else {
                    diamond_layout.setErrorEnabled(true);
                    diamond_layout.setError(" ");
                    Toast.makeText(requireContext(), "Only 2 Decimal Allow", Toast.LENGTH_SHORT).show();
                    diamond_slider.setValue(0.00f);
                }
            }
            isChanging = false;
        }
    }

    @SuppressLint("DefaultLocale")
    private void setLengthInputText(float value) {
        if(!isChanging) {
            isChanging = true;
            length_text.setText(String.format("%.2f", value));
            length_text.setSelection(Objects.requireNonNull(length_text.getText()).length());
            isChanging = false;
        }
    }
    private void setLengthSlider() {
        if(!isChanging)
        {
            isChanging = true;
            String number = Objects.requireNonNull(length_text.getText()).toString();
            if(number.isEmpty())
            {
                length_slider.setValue(0.00f);
            }
            if(number.equals("."))
            {
                number="0.0";
                length_text.setText("0.");
                length_text.setSelection(length_text.getText().length());
            }
            if(!number.isEmpty())
            {
                if (number.matches("^(\\d+\\.\\d{1,2}|\\d+|\\d+\\.)$"))
                {
                    float sliderValue = Float.parseFloat(number);
                    if(sliderValue >= 0.00 && sliderValue <= 100.00)
                    {
                        length_layout.setErrorEnabled(false);
                        length_slider.setValue(sliderValue);
                        length_text.setSelection(length_text.getText().length());
                    }
                    else {
                        length_layout.setErrorEnabled(true);
                        length_layout.setError(" ");
                        Toast.makeText(requireContext(), "Only 0.00 to 100.00 number Allow", Toast.LENGTH_SHORT).show();
                        length_slider.setValue(0.00f);
                    }
                }else {
                    length_layout.setErrorEnabled(true);
                    length_layout.setError(" ");
                    Toast.makeText(requireContext(), "Only 2 Decimal Allow", Toast.LENGTH_SHORT).show();
                    length_slider.setValue(0.00f);
                }
            }
            isChanging = false;
        }
    }

    @SuppressLint("DefaultLocale")
    private void setWidthInputText(float value) {
        if(!isChanging) {
            isChanging = true;
            width_text.setText(String.format("%.2f", value));
            width_text.setSelection(Objects.requireNonNull(width_text.getText()).length());
            isChanging = false;
        }
    }

    private void setWidthSlider() {
        if(!isChanging)
        {
            isChanging = true;
            String number = Objects.requireNonNull(width_text.getText()).toString();
            if(number.isEmpty())
            {
                width_slider.setValue(0.00f);
            }
            if(number.equals("."))
            {
                number="0.0";
                width_text.setText("0.");
                width_text.setSelection(width_text.getText().length());
            }
            if(!number.isEmpty())
            {
                if (number.matches("^(\\d+\\.\\d{1,2}|\\d+|\\d+\\.)$"))
                {
                    float sliderValue = Float.parseFloat(number);
                    if(sliderValue >= 0.00 && sliderValue <= 100.00)
                    {
                        width_layout.setErrorEnabled(false);
                        width_slider.setValue(sliderValue);
                        width_text.setSelection(width_text.getText().length());
                    }
                    else {
                        width_layout.setErrorEnabled(true);
                        width_layout.setError(" ");
                        Toast.makeText(requireContext(), "Only 0.00 to 100.00 number Allow", Toast.LENGTH_SHORT).show();
                        width_slider.setValue(0.00f);
                    }
                }else {
                    width_layout.setErrorEnabled(true);
                    width_layout.setError(" ");
                    Toast.makeText(requireContext(), "Only 2 Decimal Allow", Toast.LENGTH_SHORT).show();
                    width_slider.setValue(0.00f);
                }
            }
            isChanging = false;
        }
    }
    @SuppressLint("DefaultLocale")
    private void setHeightInputText(float value) {
        if(!isChanging) {
            isChanging = true;
            height_text.setText(String.format("%.2f", value));
            height_text.setSelection(Objects.requireNonNull(height_text.getText()).length());
            isChanging = false;
        }
    }
    private void setHeightSlider() {
        if(!isChanging)
        {
            isChanging = true;
            String number = Objects.requireNonNull(height_text.getText()).toString();
            if(number.isEmpty())
            {
                height_slider.setValue(0.00f);
            }
            if(number.equals("."))
            {
                number="0.0";
                height_text.setText("0.");
                height_text.setSelection(height_text.getText().length());
            }
            if(!number.isEmpty())
            {
                if (number.matches("^(\\d+\\.\\d{1,2}|\\d+|\\d+\\.)$"))
                {
                    float sliderValue = Float.parseFloat(number);
                    if(sliderValue >= 0.00 && sliderValue <= 100.00)
                    {
                        height_layout.setErrorEnabled(false);
                        height_slider.setValue(sliderValue);
                        height_text.setSelection(height_text.getText().length());
                    }
                    else {
                        height_layout.setErrorEnabled(true);
                        height_layout.setError(" ");
                        Toast.makeText(requireContext(), "Only 0.00 to 100.00 number Allow", Toast.LENGTH_SHORT).show();
                        height_slider.setValue(0.00f);
                    }
                }else {
                    height_layout.setErrorEnabled(true);
                    height_layout.setError(" ");
                    Toast.makeText(requireContext(), "Only 2 Decimal Allow", Toast.LENGTH_SHORT).show();
                    height_slider.setValue(0.00f);
                }
            }
            isChanging = false;
        }
    }


    private void checkData() {
        save.setBackgroundColor(requireContext().getColor(R.color.form_button_background_onClick_color));
    }

}